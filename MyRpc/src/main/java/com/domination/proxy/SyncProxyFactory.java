package com.domination.proxy;

import com.domination.cluster.*;
import com.domination.common.Invocation;
import com.domination.common.Result;
import com.domination.directory.Directory;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;

public class SyncProxyFactory extends AbstractProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass, String implName, String version, Map<String, Object> attachments) {
        AbstractProxyFactory base = new SyncProxyFactory();

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
            // 构建请求对象
            Invocation invocation = base.buildInvocation(method, args, interfaceClass, implName, version, attachments);
            invocation.setRequestId(UUID.randomUUID().toString());

            // 构建服务目录 & 集群调用器
            Directory<T> directory = (Directory<T>) base.createDirectory(interfaceClass);
            ClusterInvoker<T> clusterInvoker = (ClusterInvoker<T>) base.createClusterInvoker(directory);

            Invoker<Object> invokerChain = base.createInvokerChain(clusterInvoker);
            Result result = invokerChain.invoke(invocation);
            // 发起同步调用
//            Result result = clusterInvoker.invoke(invocation);

            if (result.hasException()) throw new RuntimeException(result.getException());

            Object value = result.getValue();
            Class<?> returnType = method.getReturnType();

            // 尽可能地类型转换
            if (returnType == void.class) return null;
            if (value == null || returnType.isInstance(value)) return value;
            if (returnType.isPrimitive()) return value;

            return returnType.cast(value); // 类型强转（通常只发生在接口是 Object）
        });
    }
}