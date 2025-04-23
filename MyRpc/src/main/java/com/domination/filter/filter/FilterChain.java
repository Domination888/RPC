package com.domination.filter.filter;

import com.domination.cluster.Invoker;
import com.domination.common.Invocation;

import java.util.List;

/**
 * FilterChain 负责串联多个 Filter 的责任链实现
 * 每个 Filter 执行完毕后，递归调用下一个 Filter
 */
public class FilterChain {
    private final List<InvokeFilter> filters;
    private final int index;
    private final Invoker invoker;

    public FilterChain(List<InvokeFilter> filters, int index, Invoker invoker) {
        this.filters = filters;
        this.index = index;
        this.invoker = invoker;
    }

    public Object invoke(Invocation invocation) throws Exception {
        // 执行完所有 Filter 后，交给最终 Invoker 执行 RPC 调用
        if (index == filters.size()) {
            return invoker.invoke(invocation);
        }

        // 调用当前 Filter，并将链推进到下一个
        return filters.get(index).invoke(invocation, new FilterChain(filters, index + 1, invoker));
    }
}