package com.domination.directory;

import com.domination.cluster.Invoker;
import com.domination.cluster.RemoteInvoker;
import com.domination.common.Invocation;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * NacosDirectory：基于 Nacos 的服务发现实现
 * 每次调用都会动态从缓存或注册中心获取最新服务实例列表。
 */
public class NacosDirectory<T> implements Directory<T> {

    private final Class<T> interfaceClass;
    private final URL url;

    public NacosDirectory(Class<T> interfaceClass, URL url) {
        this.interfaceClass = interfaceClass;
        this.url = url;
    }

    @Override
    public List<Invoker<T>> list(Invocation invocation) throws Exception {
        // 获取所有服务实例（来自缓存或 Nacos）
        List<Instance> instances = NacosServiceDiscovery.getAllInstances(interfaceClass.getName());

        // 包装为 RemoteInvoker，供 ClusterInvoker 使用
        return instances.stream()
                .map(instance -> new RemoteInvoker<>(interfaceClass, instance.getIp(), instance.getPort()))
                .collect(Collectors.toList());
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public URL getUrl() {
        return url;
    }
}