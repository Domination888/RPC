package com.domination.directory;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InstanceCache：本地缓存注册中心的服务实例
 * 避免每次请求都访问注册中心（提高性能）
 */
public class InstanceCache {
    private static final Map<String, List<Instance>> cache = new ConcurrentHashMap<>();

    public static List<Instance> get(String serviceName) {
        return cache.get(serviceName);
    }

    public static void put(String serviceName, List<Instance> instances) {
        cache.put(serviceName, instances);
    }

    public static void clear(String serviceName) {
        cache.remove(serviceName);
    }
}