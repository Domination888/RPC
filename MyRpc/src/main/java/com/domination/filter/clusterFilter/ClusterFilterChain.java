package com.domination.filter.clusterFilter;

import com.domination.cluster.Invoker;
import java.util.List;
import com.domination.common.Invocation;
import com.domination.common.Result;

public class ClusterFilterChain {

    private final List<ClusterInvokeFilter> filters;
    private final int index;
    private final Invoker<?> invoker;

    public ClusterFilterChain(List<ClusterInvokeFilter> filters, int index, Invoker<?> invoker) {
        this.filters = filters;
        this.index = index;
        this.invoker = invoker;
    }

    public Result invoke(Invocation invocation) throws Exception {
        if (index == filters.size()) {
            return invoker.invoke(invocation); // 进入 ClusterInvoker 调用
        }
        ClusterInvokeFilter filter = filters.get(index);
        return (Result) filter.invoke(invocation, new ClusterFilterChain(filters, index + 1, invoker));
    }
}
