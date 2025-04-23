package com.domination.protocol.netty;

import com.domination.common.Invocation;
import com.domination.common.RpcMessage;
import com.domination.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RpcDecoder extends LengthFieldBasedFrameDecoder {

    private final Serializer serializer;

    public RpcDecoder(Serializer serializer) {
        super(65535, 0, 4, 0, 4); // 以前 4 个字节为长度头
        this.serializer = serializer;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) return null;

        byte[] bytes = new byte[frame.readableBytes()];
        frame.readBytes(bytes);
        RpcMessage msg = serializer.deserialize(bytes, RpcMessage.class);

        return msg;
    }
}