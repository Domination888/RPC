package com.domination.common;

import java.io.Serializable;

public class RpcMessage implements Serializable {
    private String messageId;
    private byte messageType; // 0请求、1响应
    private Invocation invocation; // 请求体
    private Result result;         // 响应数据

    public RpcMessage() {
    }
    public RpcMessage(String messageId, byte messageType, Invocation invocation, Result result) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.invocation = invocation;
        this.result = result;
    }

    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public byte getMessageType() {
        return messageType;
    }
    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public Invocation getInvocation() {
        return invocation;
    }
    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

    public Result getResult() {
        return result;
    }
    public void setResult(Result result) {
        this.result = result;
    }
}
