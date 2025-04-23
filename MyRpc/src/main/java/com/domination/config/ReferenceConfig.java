package com.domination.config;

import com.domination.proxy.AsyncProxyFactory;
import com.domination.proxy.SyncProxyFactory;

import java.util.HashMap;
import java.util.Map;

public class ReferenceConfig<T> {
    private final Class<T> interfaceClass;
    private final String implName;
    private final ReferenceMetadata metadata;
    private final Map<String, Map<String, Object>> methodAttachments = new HashMap<>();

    public ReferenceConfig(Class<T> interfaceClass, String implName) {
        this.interfaceClass = interfaceClass;
        this.implName = implName;
        this.metadata = ConfigCenter.load(interfaceClass.getName(), implName);
    }
    public T getProxy(String methodName, boolean async) {
        Map<String, Object> attachments = RpcParamBuilder.build(metadata, methodName, async);
        if (async) {
            return AsyncProxyFactory.getProxy(interfaceClass, implName, metadata.getVersion(), attachments);
        } else {
            return SyncProxyFactory.getProxy(interfaceClass, implName, metadata.getVersion(), attachments);
        }
    }
}
