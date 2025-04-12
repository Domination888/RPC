package com.domination.common;

import java.io.Serializable;

public class Invocation implements Serializable {
    private String interfaceName;
    private String implName;      // 明确实现类（可选）
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String version;       // 规范使用版本
    private String callbackUrl;

    //Jackson 要求的无参构造方法
    public Invocation() {}

    public Invocation(String interfaceName, String implName, String version,
                      String methodName, Class<?>[] parameterTypes, Object[] parameters,
                      String callbackUrl) {
        this.interfaceName = interfaceName;
        this.implName = implName;
        this.version = version;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
        this.callbackUrl = callbackUrl;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getImplName() {
        return implName;
    }

    public void setImplName(String implName) {
        this.implName = implName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
