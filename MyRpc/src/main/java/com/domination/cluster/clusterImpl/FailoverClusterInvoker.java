package com.domination.cluster.clusterImpl;

import com.domination.cluster.*;
import com.domination.common.Invocation;
import com.domination.common.Result;
import com.domination.directory.Directory;
import com.domination.filter.filter.FilterChain;
import com.domination.filter.filter.InvokeFilter;
import com.domination.util.ExtensionLoader;

import java.net.URL;
import java.util.List;

public class FailoverClusterInvoker<T> implements ClusterInvoker<T> {
    private final Directory<T> directory;

    public FailoverClusterInvoker(Directory<T> directory) {
        this.directory = directory;
    }

    @Override
    public Result invoke(Invocation invocation) throws Exception {
        List<Invoker<T>> invokers = directory.list(invocation);
        if (invokers.isEmpty()) throw new RuntimeException("无可用服务提供者");

        LoadBalancer loadBalancer = ExtensionLoader.get(LoadBalancer.class);
        Invoker<T> selected = loadBalancer.select(invokers, invocation);

        // 构建 FilterChain
        List<InvokeFilter> filters = ExtensionLoader.getAll(InvokeFilter.class, "consumer");
        FilterChain chain = new FilterChain(filters, 0, selected);

        try {
            Object value = chain.invoke(invocation);
            if (value instanceof Result) {
                return (Result) value;
            } else {
                return new Result(value);
            }
        } catch (Exception e) {
            System.err.println("调用失败，尝试重试...");
            throw e;
        }
    }

    @Override
    public Class<T> getInterface() {
        return directory.getInterface();
    }

    @Override
    public URL getUrl() {
        return directory.getUrl();
    }
}
