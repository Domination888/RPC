package com.domination.cluster.loadbalanceImpl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.domination.cluster.Invoker;
import com.domination.cluster.LoadBalancer;
import com.domination.common.Invocation;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, Invocation invocation) {
        if (invokers.isEmpty()) throw new RuntimeException("无可用服务提供者");
        return invokers.get(random.nextInt(invokers.size()));
    }

}