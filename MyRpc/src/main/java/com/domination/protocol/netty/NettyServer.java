package com.domination.protocol.netty;

import com.domination.serialize.Serializer;
import com.domination.serialize.SerializerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;

public class NettyServer {

    private static final Serializer serializer = SerializerFactory.getFromConfig();

    public static void start(int port) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcDecoder(serializer));  // 把 ByteBuf 转成 RpcMessage
                            p.addLast(new RpcEncoder(serializer));  // 将 RpcMessage 转回 ByteBuf（含长度）
                            p.addLast(new NettyServerHandler());   // 业务处理
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Netty Server 启动成功，监听端口：" + port);
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}