package com.domination.common;

import java.io.Serializable;

public class Invocation implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] parameters;
    public Invocation(String interfaceName, String methodName, Class[] parameterTypes, Object[] parameters) {
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
        this.methodName = methodName;
        this.interfaceName = interfaceName;
    }
    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

}
