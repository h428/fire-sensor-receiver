package com.hao.work.handler;

import com.hao.work.entity.SensorData;
import com.hao.work.manager.FileWriterManager;
import com.hao.work.view.DataCollector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Set<Integer> INTERESTED_NODE_IDS = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    private final DataCollector dataCollector;

    public ServerHandler(DataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("接入新的传感器");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() < 16) {
            log.warn("接收到的数据长度不足");
            return;
        }

        // 读取传感器数据
        SensorData sensorData = new SensorData();
        sensorData.setMagicNumber(buf.readUnsignedShort());
        sensorData.setNodeId(buf.readUnsignedShort());
        sensorData.setCo2Concentration(buf.readUnsignedShort());
        sensorData.setTemperature(buf.readUnsignedShort());
        sensorData.setSmokeConcentration(buf.readUnsignedShort());
        sensorData.setX(buf.readUnsignedShort());
        sensorData.setY(buf.readUnsignedShort());
        sensorData.setZ(buf.readUnsignedShort());

        // 获取当前时间戳
        long now = Instant.now().toEpochMilli();
        sensorData.setTimestamp(now);

        // 整合日志信息到一行
        log.info("[{}] 读取到数据: {}", sensorData.getFormattedTimestamp(), sensorData);

        if (INTERESTED_NODE_IDS.contains(sensorData.getNodeId())) {
            dataCollector.collect(sensorData);
        }

        String csvLine = sensorData.toCsvLine();
        FileWriterManager.writeData(csvLine);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Error occurred during processing", cause);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }
}
