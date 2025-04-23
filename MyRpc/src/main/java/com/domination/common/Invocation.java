package com.domination.common;

import java.io.Serializable;
import java.util.Map;

public class Invocation implements Serializable {
    private String interfaceName;
    private String implName;      // 明确实现类
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String version;       // 规范使用版本
    private Map<String, Object> attachments; // 附加配置，比如 timeout、retries 等
    private String requestId;

    public Invocation() {}

    public Invocation(String interfaceName, String implName, String version,
                      String methodName, Class<?>[] parameterTypes, Object[] parameters,
                      Map<String, Object> attachments) {
        this.interfaceName = interfaceName;
        this.implName = implName;
        this.version = version;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
        this.attachments = attachments;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getImplName() {
        return implName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setImplName(String implName) {
        this.implName = implName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }
}
