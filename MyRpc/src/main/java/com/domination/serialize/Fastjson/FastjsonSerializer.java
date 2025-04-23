package com.domination.serialize.Fastjson;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.domination.serialize.Serializer;
import com.alibaba.fastjson2.JSON;

public class FastjsonSerializer implements Serializer {
    public FastjsonSerializer() {
    }

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz, JSONReader.Feature.SupportClassForName);
    }

    @Override
    public String name() {
        return "fastjson";
    }
}