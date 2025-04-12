package com.domination.monitor;

import com.domination.register.NacosServiceRegister;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时向 Nacos 注册自身，实现心跳保活
 */
public class Heartbeat {

    public static void start(String serviceName, String host, int port) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                NacosServiceRegister.register(serviceName, host, port);
                System.out.println("[Heartbeat] " + serviceName + " 心跳续租成功");
            } catch (Exception e) {
                System.err.println("[Heartbeat] 心跳失败：" + e.getMessage());
            }
        }, 0, 5, TimeUnit.SECONDS); // 每 5 秒续租一次
    }
}