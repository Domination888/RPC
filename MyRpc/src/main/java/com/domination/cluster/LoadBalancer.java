package com.domination.cluster;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.domination.common.Invocation;

import java.util.List;


public interface LoadBalancer {
    <T> Invoker<T> select(List<Invoker<T>> invokers, Invocation invocation);
}
