package com.domination.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

public class ConfigFetcher {
    private static ConfigService configService;

    static {
        try {
            Properties props = new Properties();
            props.setProperty("serverAddr", "localhost:8848");
            configService = NacosFactory.createConfigService(props);
        } catch (NacosException e) {
            throw new RuntimeException("初始化 Nacos ConfigService 失败", e);
        }
    }

    public static String getConfig(String dataId, String group) throws NacosException {
        return configService.getConfig(dataId, group, 3000);
    }
}
