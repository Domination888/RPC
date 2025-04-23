package com.domination.monitor;

import com.domination.common.HeartbeatMessage;
import io.netty.channel.Channel;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatClient {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void start(Channel channel) {
        scheduler.scheduleAtFixedRate(() -> {
            if (channel.isActive()) {
                String msgId = UUID.randomUUID().toString();
                HeartbeatMessage ping = new HeartbeatMessage(msgId, HeartbeatMessage.TYPE_PING);
                channel.writeAndFlush(ping);
            }
        }, 0, 30, TimeUnit.SECONDS); // 每 30 秒发一次
    }
}
