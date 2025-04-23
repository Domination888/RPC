package com.domination.common;

public class Result {
    private Object value;
    private Throwable exception;

    public Result() {}
    public Result(Object value) {
        this.value = value;
    }
    public Result(Throwable exception) {
        this.exception = exception;
    }

    public Object getValue() {
        return value;
    }
    public Throwable getException() {
        return exception;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean hasException() {
        return exception != null;
    }
}