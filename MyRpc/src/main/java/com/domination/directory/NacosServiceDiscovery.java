package com.domination.directory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * NacosServiceDiscovery：对 Nacos 注册中心的服务查询封装
 * 使用 InstanceCache 做本地缓存，支持事件驱动刷新。
 */
public class NacosServiceDiscovery {
    private static final NamingService naming;

    static {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", "localhost:8848");
            naming = NacosFactory.createNamingService(properties);
        } catch (Exception e) {
            throw new RuntimeException("初始化 Nacos 失败", e);
        }
    }

    // 获取一个实例（随机负载均衡）
    public static Instance getOneInstance(String serviceName) throws NacosException {
        List<Instance> instances = getAllInstances(serviceName);
        return instances.get(new Random().nextInt(instances.size()));
    }

    // 获取全部实例（使用缓存 + 动态监听）
    public static List<Instance> getAllInstances(String serviceName) throws NacosException {
        List<Instance> instances = InstanceCache.get(serviceName);
        if (instances == null) {
            instances = naming.getAllInstances(serviceName);
            InstanceCache.put(serviceName, instances);

            // 注册 Nacos 事件监听，刷新本地缓存
            naming.subscribe(serviceName, event -> {
                NamingEvent namingEvent = (NamingEvent) event;
                List<Instance> newInstances = namingEvent.getInstances();
                System.out.println("服务 [" + serviceName + "] 实例已更新，数量：" + newInstances.size());
                InstanceCache.put(serviceName, newInstances);
            });
        }
        return instances;
    }
}