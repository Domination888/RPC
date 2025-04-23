package com.domination.common;

public class HeartbeatMessage extends RpcMessage {
    public static final byte TYPE_PING = 2;
    public static final byte TYPE_PONG = 3;

    public HeartbeatMessage(String messageId, byte type) {
        this.setMessageId(messageId);
        this.setMessageType(type); // 2 = ping, 3 = pong
    }
}