package com.domination.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {
    private static final Map<String, Class<?>> map = new HashMap<>();

    public static void regist(String interfaceName, String key, Class<?> implClass) {
        map.put(interfaceName + "#" + key, implClass);
    }

    public static Class<?> get(String interfaceName, String key) {
        return map.get(interfaceName + "#" + key);
    }
}
