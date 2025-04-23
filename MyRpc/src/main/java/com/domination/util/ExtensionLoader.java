package com.domination.util;

import com.domination.common.Activate;

import java.util.*;

public class ExtensionLoader {
    private static final Map<Class<?>, Object> cached = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAll(Class<T> type, String group) {
        List<T> list = new ArrayList<>();
        ServiceLoader<T> loader = ServiceLoader.load(type);
        for (T impl : loader) {
            Activate activate = impl.getClass().getAnnotation(Activate.class);
            if (activate == null || "".equals(group) || group.equals(activate.group())) {
                list.add(impl);
            }
        }
        return list;
    }

    public static <T> T get(Class<T> type) {
        ServiceLoader<T> loader = ServiceLoader.load(type);
        for (T impl : loader) return impl;
        throw new RuntimeException("未找到类型：" + type.getName());
    }
}