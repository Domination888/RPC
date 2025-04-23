package com.domination.protocol.netty;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class FutureRegistry {
    private static final Map<String, CompletableFuture<Object>> futureMap = new ConcurrentHashMap<>();

    public static void register(String requestId, CompletableFuture<Object> future) {
        futureMap.put(requestId, future);
    }
    public static void remove(String requestId) {
        futureMap.remove(requestId);
    }
    public static void complete(String requestId, Object result) {
        CompletableFuture<Object> future = futureMap.remove(requestId);
        if (future != null) {
            future.complete(result);
//            System.out.println("future完成：" + requestId + "result:" + result);
        } else {
            System.err.println("未找到 Future, ID=" + requestId);
        }
    }
    public static CompletableFuture<Object> get(String requestId) {
        return futureMap.get(requestId);
    }
}