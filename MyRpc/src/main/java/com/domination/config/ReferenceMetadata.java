package com.domination.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceMetadata {
    private String version;
    private Map<String, MethodConfig> methods;

    public ReferenceMetadata() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, MethodConfig> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, MethodConfig> methods) {
        this.methods = methods;
    }

    public MethodConfig getMethod(String methodName) {
        return methods.get(methodName);
    }
}