package com.hao.work.client;

import com.hao.work.entity.SensorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.Unpooled;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvSensorClientHandler extends ChannelInboundHandlerAdapter {
    private final List<SensorData> sensorDataList;
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public CsvSensorClientHandler(List<SensorData> sensorDataList) {
        this.sensorDataList = sensorDataList;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.executor().scheduleAtFixedRate(() -> {
            int index = currentIndex.getAndIncrement();
            if (index < sensorDataList.size()) {
                SensorData data = sensorDataList.get(index);
                ctx.writeAndFlush(Unpooled.copiedBuffer(data.toByteArray()));
            } else {
                // 如果所有数据都发送完毕，可以选择关闭连接或循环发送
                // ctx.close();
                currentIndex.set(0); // 或者重置索引以循环发送
            }
        }, 0, 500, TimeUnit.MILLISECONDS); // 每秒发送一次
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

