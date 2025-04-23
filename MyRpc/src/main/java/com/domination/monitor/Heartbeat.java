package com.domination.monitor;

import com.domination.register.NacosServiceRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时向 Nacos 注册自身，实现心跳保活
 */
public class Heartbeat {

    // 使用单线程定时器
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // 维护所有需要心跳的服务 <serviceName, [host:port]>
    private static final Map<String, String> serviceMap = new ConcurrentHashMap<>();

    static {
        // 启动统一的心跳线程
        scheduler.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, String> entry : serviceMap.entrySet()) {
                String serviceName = entry.getKey();
                String address = entry.getValue();
                String[] parts = address.split(":");
                try {
                    NacosServiceRegister.register(serviceName, parts[0], Integer.parseInt(parts[1]));
                    System.out.println("[Heartbeat] " + serviceName + " 心跳续租成功");
                } catch (Exception e) {
                    System.err.println("[Heartbeat] 心跳失败：" + serviceName + " - " + e.getMessage());
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static void start(String serviceName, String host, int port) {
        serviceMap.put(serviceName, host + ":" + port);
    }

    public static void stop(String serviceName) {
        serviceMap.remove(serviceName);
    }
}