package com.domination.util;// NacosServiceDiscovery.java
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Properties;

public class NacosServiceDiscovery {
    public static Instance getOneInstance(String serviceName) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", "localhost:8848");
        NamingService naming = NacosFactory.createNamingService(properties);
        List<Instance> instances = naming.getAllInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("未发现服务：" + serviceName);
        }
        return instances.get(0); // 简单选第一个，可改为随机或负载均衡策略
    }
}