package com.hao.work.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Splitter extends FixedLengthFrameDecoder {

    static final int MAGIC_NUMBER = 0xABCD;

    public Splitter() {
        super(16);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        if (in.getUnsignedShort(in.readerIndex()) != MAGIC_NUMBER) {
            log.info("数据帧魔数非法，关闭连接");
            ctx.channel().close(); // 关闭连接
            return null;
        }

        return super.decode(ctx, in);
    }
}
