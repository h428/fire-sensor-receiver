package com.hao.work.client;

import com.hao.work.entity.SensorData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

public class SensorClient {
    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 9527; // 服务端端口
        String csvFilePath = "E:\\code\\java\\java-work\\fire-sensor-receiver\\src\\main\\resources\\sensor_data_20240709_153642.csv"; // CSV文件路径

        List<SensorData> sensorDataList = SensorDataReader.readSensorDataFromCsv(csvFilePath);

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CsvSensorClientHandler(sensorDataList));
                        }
                    });

            // 启动客户端
            b.connect(host, port).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}

