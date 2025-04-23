package com.domination.protocol.netty;

import com.domination.common.HeartbeatMessage;
import com.domination.common.Result;
import com.domination.common.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private volatile long lastReadTime = System.currentTimeMillis();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) {
        if (msg.getMessageType() == HeartbeatMessage.TYPE_PONG) {
            lastReadTime = System.currentTimeMillis(); // 更新时间戳
//            System.out.println("收到 PONG");
            return;
        }
        Result result = msg.getResult();
        if (result != null) {
            String requestId = msg.getMessageId();
//            System.out.println("收到回应：requestId为:" + requestId + "值为：" + result.getValue());
            FutureRegistry.complete(requestId, result.getValue());
        } else {
            System.err.println("结果为空，消息ID: " + msg.getMessageId());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        long now = System.currentTimeMillis();
        if (now - lastReadTime > 60_000) { // 超过 60 秒未收到任何消息
            System.err.println("服务端无响应，关闭连接...");
            ctx.close(); // 关闭连接，可触发重连
        }
        super.userEventTriggered(ctx, evt);
    }
}
