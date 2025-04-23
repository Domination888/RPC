package com.domination.filter.filter.impl;

import com.domination.common.Activate;
import com.domination.common.Invocation;
import com.domination.filter.filter.FilterChain;
import com.domination.filter.filter.InvokeFilter;
@Activate(group = "consumer")
public class RetryFilter implements InvokeFilter {

    @Override
    public Object invoke(Invocation invocation, FilterChain chain) throws Exception {
        // 如果是异步调用，则不重试
        Object asyncFlag = invocation.getAttachments().get("async");
        if (asyncFlag instanceof Boolean && (Boolean) asyncFlag) {
            return chain.invoke(invocation); // 直接执行一次
        }
//        System.out.println("RetryFilter invoke");
        int retries = (int) invocation.getAttachments().getOrDefault("retries", 0);
        Exception lastEx = null;

        for (int i = 0; i <= retries; i++) {
            try {
                return chain.invoke(invocation);
            } catch (Exception e) {
                lastEx = e;
                System.err.println("[RetryFilter] 第 " + (i + 1) + " 次调用失败：" + e.getMessage());
            }
        }

        throw lastEx;
    }
}