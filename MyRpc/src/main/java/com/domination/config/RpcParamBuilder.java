package com.domination.config;

import java.util.HashMap;
import java.util.Map;

public class RpcParamBuilder {
    public static Map<String, Object> build(ReferenceMetadata meta, String methodName, boolean async) {
        Map<String, Object> attachments = new HashMap<>();
        MethodConfig conf = meta.getMethod(methodName);
        attachments.put("timeout", conf.getTimeout());
        attachments.put("retries", conf.getRetries());
        attachments.put("rateLimit", conf.getRateLimit());
        attachments.put("async", async);
        attachments.put("authToken", "secure-token");//TODO:换成真正读取token，这里写死，与clusterFilter的Auth呼应
        return attachments;
    }
}