package com.domination.protocol.netty;

import com.domination.cluster.Invoker;
import com.domination.common.HeartbeatMessage;
import com.domination.common.Invocation;
import com.domination.common.Result;
import com.domination.common.RpcMessage;
import com.domination.filter.filter.FilterChain;
import com.domination.filter.filter.InvokeFilter;
import com.domination.register.LocalRegister;
import com.domination.util.ExtensionLoader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

    // 使用业务线程池处理请求，避免 I/O 线程阻塞
    private static final ExecutorService bizPool = Executors.newFixedThreadPool(8);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        if (msg.getMessageType() == 0) { // 普通请求类型
            // 把整个业务处理逻辑放到线程池里执行，释放 Netty I/O 线程
            bizPool.execute(() -> {
                try {
                    Invocation invocation = msg.getInvocation();
                    String interfaceName = invocation.getInterfaceName();
                    String implKey = invocation.getImplName() + ":" + invocation.getVersion();

                    // 查找本地服务实现类
                    Class<?> implClass = LocalRegister.get(interfaceName, implKey);
                    if (implClass == null) {
                        throw new RuntimeException("未找到服务实现：" + implKey);
                    }

                    // 反射构造服务实例并调用方法
                    Object implInstance = implClass.getDeclaredConstructor().newInstance();
                    Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParameterTypes());

                    Invoker<?> invoker = (Invoker<Object>) inv -> {
                        try {
                            Object result = method.invoke(implInstance, inv.getParameters());
                            if (result instanceof CompletableFuture<?>) {
                                result = ((CompletableFuture<?>) result).get(); // 等待异步执行完
                            }
                            if (method.getReturnType() == void.class) {
                                result = "void"; // 避免客户端获取到 null
                            }
                            return new Result(result);
                        } catch (Exception e) {
                            return new Result(e);
                        }
                    };

                    // 构建 Filter 链
                    List<InvokeFilter> filters = ExtensionLoader.getAll(InvokeFilter.class, "provider");
                    FilterChain chain = new FilterChain(filters, 0, invoker);

                    // 执行 Filter 链
                    Result result;
                    try {
                        Object value = chain.invoke(invocation);
                        result = (value instanceof Result) ? (Result) value : new Result(value);
                    } catch (Exception e) {
                        result = new Result(e);
                    }

                    // 返回结果给客户端
                    RpcMessage response = RpcMessageFactory.buildResponse(msg.getMessageId(), result);
                    ctx.writeAndFlush(response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (msg.getMessageType() == HeartbeatMessage.TYPE_PING) {
            HeartbeatMessage pong = new HeartbeatMessage(msg.getMessageId(), HeartbeatMessage.TYPE_PONG);
            ctx.writeAndFlush(pong); // 回复 PONG
        }
    }
}