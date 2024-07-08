package com.hao.work.server;

import com.hao.work.handler.ServerHandler;
import com.hao.work.manager.FileWriterManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    private static final int PORT = 9527;

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new Splitter(), new ServerHandler());
                        }
                    });

            log.info("Server is starting on port {}", PORT);
            serverBootstrap.bind(PORT).channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Server interrupted: ", e);
            Thread.currentThread().interrupt(); // Restore interrupted status
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            try {
                FileWriterManager.closeWriter();
            } catch (IOException e) {
                log.error("Failed to close FileWriter: ", e);
            }
            log.info("Server shutdown complete.");
        }
    }

}
