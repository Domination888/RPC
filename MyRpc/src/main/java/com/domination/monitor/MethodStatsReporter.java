package com.domination.monitor;

import com.domination.filter.filter.impl.MethodCounterFilter;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MethodStatsReporter {

    public static void startMonitoring() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Map<String, Long> snapshot = MethodCounterFilter.getSnapshot();
            System.out.println("[监控] 方法调用统计（每5秒）：");
            snapshot.forEach((key, count) -> System.out.println("  - " + key + " = " + count + " 次"));
        }, 5, 5, TimeUnit.SECONDS); // 首次延迟5秒，之后每5秒打印一次
    }
}
