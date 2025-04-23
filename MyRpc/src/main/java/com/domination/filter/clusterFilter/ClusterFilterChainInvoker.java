package com.domination.filter.clusterFilter;

import com.domination.cluster.ClusterInvoker;
import com.domination.cluster.Invoker;
import com.domination.common.Invocation;
import com.domination.common.Result;

import java.net.URL;
import java.util.List;


public class ClusterFilterChainInvoker implements Invoker<Object> {
    private final ClusterInvoker<?> clusterInvoker;
    private final List<ClusterInvokeFilter> filters;

    public ClusterFilterChainInvoker(ClusterInvoker<?> clusterInvoker, List<ClusterInvokeFilter> filters) {
        this.clusterInvoker = clusterInvoker;
        this.filters = filters;
    }

    @Override
    public Result invoke(Invocation invocation) throws Exception {
        return new ClusterFilterChain(filters, 0, clusterInvoker).invoke(invocation);
    }

    @Override
    public Class<Object> getInterface() {
        return Object.class;
    }

    @Override
    public URL getUrl() {
        return clusterInvoker.getUrl();
    }
}
