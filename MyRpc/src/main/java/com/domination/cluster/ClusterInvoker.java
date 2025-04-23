package com.domination.cluster;

import com.domination.common.Invocation;
import com.domination.common.Result;


/**
 * ClusterInvoker 是一个聚合多个 Invoker 的虚拟 Invoker，
 * 通常用于处理集群容错、负载均衡、路由等逻辑。
 */
public interface ClusterInvoker<T> extends Invoker<T> {

    /**
     * 聚合调用（可带重试、容错、负载均衡）
     */
    @Override
    Result invoke(Invocation invocation) throws Exception;

    /**
     * 获取目标接口类
     */
    @Override
    Class<T> getInterface();
}