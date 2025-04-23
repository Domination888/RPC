package com.domination.filter.filter.impl;

import com.domination.common.Activate;
import com.domination.common.Invocation;
import com.domination.filter.filter.FilterChain;
import com.domination.filter.filter.InvokeFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Activate(group = "provider")
public class MethodCounterFilter implements InvokeFilter {

    private static final Map<String, AtomicLong> totalCountMap = new ConcurrentHashMap<>();

    @Override
    public Object invoke(Invocation invocation, FilterChain chain) throws Exception {
        String methodKey = invocation.getInterfaceName() + "#" + invocation.getMethodName();
        totalCountMap.computeIfAbsent(methodKey, k -> new AtomicLong(0)).incrementAndGet();
        return chain.invoke(invocation);
    }

    public static Map<String, Long> getSnapshot() {
        Map<String, Long> copy = new HashMap<>();
        totalCountMap.forEach((k, v) -> copy.put(k, v.get()));
        return copy;
    }

    public static void reset() {
        totalCountMap.clear();
    }
}
