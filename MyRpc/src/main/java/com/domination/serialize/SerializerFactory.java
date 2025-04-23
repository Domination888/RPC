package com.domination.serialize;

import java.util.*;

public class SerializerFactory {
    private static final Map<String, Serializer> serializerMap = new HashMap<>();
    private static final String CONFIG_KEY = "serializer";
    public static final String DEFAULT_SERIALIZER = "fastjson";

    static {
        ServiceLoader<Serializer> loader = ServiceLoader.load(Serializer.class);
        for (Serializer serializer : loader) {
            serializerMap.put(serializer.name(), serializer);
        }
    }

    public static Serializer get(String name) {
        return serializerMap.getOrDefault(name, serializerMap.get("fastjson"));
    }

    public static Serializer getFromConfig() {
        String name = System.getProperty(CONFIG_KEY, DEFAULT_SERIALIZER);
        System.out.println("[DEBUG] 当前配置的 serializer = " + name);
        return get(name);
    }
}