package com.domination.filter.filter.impl;

import com.domination.common.Activate;
import com.domination.common.Invocation;
import com.domination.filter.filter.FilterChain;
import com.domination.filter.filter.InvokeFilter;

import java.util.concurrent.*;
@Activate(group = "consumer")
public class TimeoutFilter implements InvokeFilter {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public Object invoke(Invocation invocation, FilterChain chain) throws Exception {
        int timeoutMillis = (int) invocation.getAttachments().getOrDefault("timeout", 1000); // 可从配置中心获取
        Future<Object> future = executor.submit(() -> chain.invoke(invocation));
        try {
            return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new RuntimeException("方法调用超时（" + timeoutMillis + "ms）", e);
        }
    }
}