package com.domination.filter.filter.impl;

import com.domination.common.Activate;
import com.domination.common.Invocation;
import com.domination.filter.filter.FilterChain;
import com.domination.filter.filter.InvokeFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Activate(group = "provider")
public class RateLimitFilter implements InvokeFilter {

    private static final Map<String, AtomicInteger> currentMap = new ConcurrentHashMap<>();

    @Override
    public Object invoke(Invocation invocation, FilterChain chain) throws Exception {
        String methodKey = invocation.getInterfaceName() + "#" + invocation.getMethodName();
        int rateLimit = (int) invocation.getAttachments().getOrDefault("rateLimit", 100);

        int current = currentMap.computeIfAbsent(methodKey, k -> new AtomicInteger(0)).incrementAndGet();
        try {
            if (current > rateLimit) {
                throw new RuntimeException("方法调用超过限流阈值：" + methodKey);
            }
            return chain.invoke(invocation);
        } finally {
            currentMap.get(methodKey).decrementAndGet();
        }
    }
}
