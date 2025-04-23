package com.domination.proxy;

import com.domination.cluster.ClusterInvoker;
import com.domination.cluster.Invoker;
import com.domination.directory.Directory;
import com.domination.common.Invocation;
import com.domination.protocol.netty.RpcContext;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AsyncProxyFactory extends AbstractProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass, String implName, String version, Map<String, Object> attachments) {
        AbstractProxyFactory base = new AsyncProxyFactory();

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
            // 构建请求对象
            Invocation invocation = base.buildInvocation(method, args, interfaceClass, implName, version, attachments);
            String requestId = UUID.randomUUID().toString();
            invocation.setRequestId(requestId);

            // 初始化上下文中的 Future（供外部使用）
            CompletableFuture<Object> future = new CompletableFuture<>();
            RpcContext.getContext().setRequestId(requestId);
            RpcContext.getContext().setFuture(future);

            // 构建服务目录 & 集群调用器
            Directory<T> directory = (Directory<T>) base.createDirectory(interfaceClass);
            ClusterInvoker<T> clusterInvoker = (ClusterInvoker<T>) base.createClusterInvoker(directory);

            Invoker<Object> invokerChain = base.createInvokerChain(clusterInvoker);
            invokerChain.invoke(invocation);
            // 发起异步调用（实际由 Netty 异步返回结果）
//            clusterInvoker.invoke(invocation);

            // 返回同步接口默认值（占位）
            return getDefaultReturnValue(method.getReturnType());
        });
    }
}