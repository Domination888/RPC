package com.domination.monitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MethodCounter {

    private static final ConcurrentMap<String, AtomicInteger> counterMap = new ConcurrentHashMap<>();

    public static void record(String interfaceName, String methodName) {
        String key = interfaceName + "#" + methodName;
        counterMap.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public static void printAll() {
        System.out.println("========= 方法调用统计 =========");
        counterMap.forEach((key, value) ->
                System.out.println("[Monitor] " + key + " called " + value.get() + " times")
        );
    }

    public static int getCount(String interfaceName, String methodName) {
        return counterMap.getOrDefault(interfaceName + "#" + methodName, new AtomicInteger(0)).get();
    }
}