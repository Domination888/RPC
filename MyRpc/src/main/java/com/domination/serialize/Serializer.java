package com.domination.serialize;

public interface Serializer {
    <T> byte[] serialize(T obj) throws Exception;
    <T> T deserialize(byte[] data, Class<T> clazz) throws Exception;

    String name(); // 用于选择，例如 "jackson", "fastjson"
}