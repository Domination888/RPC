package com.domination.protocol.netty;

import com.domination.common.RpcMessage;
import com.domination.monitor.HeartbeatClient;
import com.domination.serialize.Serializer;
import com.domination.serialize.SerializerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static final Serializer serializer = SerializerFactory.getFromConfig();
    private static final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    // 发起异步请求
    public static void sendAsync(String host, int port, RpcMessage msg) {
        String address = host + ":" + port;
        Channel channel = channelMap.computeIfAbsent(address, addr -> connect(host, port));
        HeartbeatClient.start(channel);
//        System.out.println("发出请求msg：" + msg.getInvocation().getInterfaceName() + msg.getInvocation().getMethodName());
        channel.writeAndFlush(msg);
    }

    private static Channel connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS)); // 每 30s 检测一次
                            p.addLast(new RpcDecoder(serializer));
                            p.addLast(new RpcEncoder(serializer));
                            p.addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(host, port).sync();
            return f.channel();

        } catch (Exception e) {
            throw new RuntimeException("NettyClient 连接失败", e);
        }
    }
}