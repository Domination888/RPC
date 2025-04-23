package com.domination.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.domination.serialize.Serializer;
import com.domination.serialize.SerializerFactory;

import java.util.Map;
import java.util.Properties;

public class ConfigCenter {
    private static final Serializer serializer = SerializerFactory.getFromConfig();

    public static ReferenceMetadata load(String interfaceName, String implName) {
        try {
            String dataId = "meta." + interfaceName;
            String group = "DEFAULT_GROUP";
            String configStr = ConfigFetcher.getConfig(dataId, group);

            Map<String, Object> rawMap = serializer.deserialize(configStr.getBytes(), Map.class);
            Object implRaw = rawMap.get(implName);
            byte[] json = serializer.serialize(implRaw);

            return serializer.deserialize(json, ReferenceMetadata.class);
        } catch (Exception e) {
            throw new RuntimeException("加载配置失败", e);
        }
    }
}