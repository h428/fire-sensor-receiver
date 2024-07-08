package com.hao.work;

import com.hao.work.handler.ServerHandler;
import com.hao.work.view.DataCollector;
import com.hao.work.view.RealTimeChartApp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import javafx.application.Application;

public class App {
    public static void main(String[] args) {

        // 启动 JavaFX 应用
        new Thread(() -> Application.launch(RealTimeChartApp.class, args)).start();

        try {
            // 等待 JavaFX 应用初始化
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取 RealTimeChartApp 实例
        RealTimeChartApp chartApp = RealTimeChartApp.getInstance();
        DataCollector dataCollector = new DataCollector(chartApp);

        // 启动 Netty 服务器
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ServerHandler(dataCollector));
                        }
                    });

            b.bind(9527).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
