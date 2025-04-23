package com.domination.filter.clusterFilter;

import com.domination.common.Invocation;

/**
 * ClusterInvokeFilter：作用于 ClusterInvoker 之前的消费端调用链
 * 目的：统一做日志、鉴权、参数校验、限流等逻辑
 */
public interface ClusterInvokeFilter {
    Object invoke(Invocation invocation, ClusterFilterChain chain) throws Exception;
}
