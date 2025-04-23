package com.domination.protocol.netty;

import java.util.concurrent.CompletableFuture;

public class RpcContext {
    private static final ThreadLocal<RpcContext> LOCAL = ThreadLocal.withInitial(RpcContext::new);
    private String requestId;
    private CompletableFuture<Object> future;

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public CompletableFuture<Object> getFuture() {
        return future;
    }
    public void setFuture(CompletableFuture<Object> future) {
        this.future = future;
    }

    public void clear() {
        future = null;
        requestId = null;
    }
}