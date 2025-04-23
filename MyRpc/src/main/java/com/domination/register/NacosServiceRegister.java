package com.domination.register;// NacosServiceRegister.java
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;

import java.util.Properties;

public class NacosServiceRegister {
    public static void register(String serviceName, String ip, int port) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", "localhost:8848");
        NamingService naming = NacosFactory.createNamingService(properties);
        naming.registerInstance(serviceName, ip, port);
        System.out.println("注册服务成功：" + serviceName + " -> " + ip + ":" + port);
    }
}