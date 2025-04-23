package com.domination.protocol.netty;

import com.domination.common.Invocation;
import com.domination.common.Result;
import com.domination.common.RpcMessage;

import java.util.UUID;

public class RpcMessageFactory {
    public static RpcMessage buildRequest(Invocation inv) {
        return new RpcMessage(
                "default",
                (byte) 0, // 0: 请求
                inv,
                null
        );
    }

    public static RpcMessage buildResponse(String messageId, Result result) {
        return new RpcMessage(
                messageId,
                (byte) 1, // 1: 响应
                null,
                result
        );
    }
}