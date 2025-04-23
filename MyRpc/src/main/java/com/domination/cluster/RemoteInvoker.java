package com.domination.cluster;

import com.domination.common.Invocation;
import com.domination.common.Result;
import com.domination.common.RpcMessage;
import com.domination.protocol.netty.FutureRegistry;
import com.domination.protocol.netty.RpcContext;
import com.domination.protocol.netty.RpcMessageFactory;
import com.domination.protocol.netty.NettyClient;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RemoteInvoker<T> implements Invoker<T> {

    private final Class<T> interfaceClass;
    private final String ip;
    private final int port;

    public RemoteInvoker(Class<T> interfaceClass, String ip, int port) {
        this.interfaceClass = interfaceClass;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public Result invoke(Invocation invocation) {
        try {
            RpcMessage msg = RpcMessageFactory.buildRequest(invocation);
            msg.setMessageId(invocation.getRequestId());
            // 统一 Future 模型（支持 Dubbo 风格预注册）
            CompletableFuture<Object> future = RpcContext.getContext().getFuture();
            if (future == null) {
                // 如果没有预注册（比如同步调用），就自己创建
                future = new CompletableFuture<>();
                RpcContext.getContext().setFuture(future);
            }
            FutureRegistry.register(msg.getMessageId(), future);
//            System.out.println("注册future，ID为：" + msg.getMessageId() + "future为：" + future);
            NettyClient.sendAsync(ip, port, msg);

            // 异步调用：只注册 Future，不等待
            if (Boolean.TRUE.equals(invocation.getAttachments().get("async"))) {
                return null;
            }

            // 同步调用：直接阻塞等待结果
            int timeout = (int) invocation.getAttachments().getOrDefault("timeout", 3000);
            Object result = future.get(timeout, TimeUnit.MILLISECONDS);
            return new Result(result);

        } catch (Exception e) {
            return new Result(e);
        }
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public URL getUrl() {
        // 如果需要，可以创建 URL 对象封装元信息
        return null;
    }
}