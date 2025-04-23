package com.domination.proxy;

import com.domination.cluster.ClusterInvoker;
import com.domination.cluster.Invoker;
import com.domination.common.Result;
import com.domination.directory.Directory;
import com.domination.directory.NacosDirectory;
import com.domination.cluster.clusterImpl.FailoverClusterInvoker;
import com.domination.common.Invocation;
import com.domination.filter.clusterFilter.ClusterFilterChain;
import com.domination.filter.clusterFilter.ClusterFilterChainInvoker;
import com.domination.filter.clusterFilter.ClusterInvokeFilter;
import com.domination.util.ExtensionLoader;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public abstract class AbstractProxyFactory {

    // 构建一次完整的 Invocation 请求体
    protected Invocation buildInvocation(Method method, Object[] args, Class<?> interfaceClass,
                                         String implName, String version, Map<String, Object> attachments) {
        return new Invocation(
                interfaceClass.getName(),
                implName,
                version,
                method.getName(),
                method.getParameterTypes(),
                args,
                attachments
        );
    }

    // 构建服务目录，用于服务发现
    protected Directory<?> createDirectory(Class<?> interfaceClass) throws MalformedURLException {
        return new NacosDirectory<>(interfaceClass, new URL("http://localhost")); // TODO: 可支持 config center 动态配置
    }

    // 构建集群调用器，默认使用 Failover 策略
    protected ClusterInvoker<?> createClusterInvoker(Directory<?> directory) {
        return new FailoverClusterInvoker<>(directory);
    }

    // 返回同步接口的默认值，用于异步调用返回占位
    protected static Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType == void.class) return null;
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) return false;
            if (returnType == char.class) return '\0';
            return 0;
        }
        return null;
    }

    // 构建完整的 Invoker 链（添加 ClusterFilter）
    protected Invoker<Object> createInvokerChain(ClusterInvoker<?> clusterInvoker) {
        List<ClusterInvokeFilter> filters = ExtensionLoader.getAll(ClusterInvokeFilter.class, "cluster");
        return new ClusterFilterChainInvoker(clusterInvoker, filters);
    }
}