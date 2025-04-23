package com.domination.serialize.Jackson;

import com.domination.serialize.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonSerializer implements Serializer {
    public JacksonSerializer() {
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return mapper.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        return mapper.readValue(data, clazz);
    }

    @Override
    public String name() {
        return "jackson";
    }
}