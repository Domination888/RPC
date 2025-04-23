package com.domination.directory;

import com.domination.cluster.Invoker;
import com.domination.common.Invocation;

import java.net.URL;
import java.util.List;

/**
 * Directory：服务目录，封装服务发现逻辑（比如从注册中心获取可用服务实例）
 * ClusterInvoker 调用时会从 Directory 获取 Invoker 列表。
 */
public interface Directory<T> {

    /**
     * 获取当前服务的所有可用 Invoker（可做路由筛选）
     */
    List<Invoker<T>> list(Invocation invocation) throws Exception;

    /**
     * 返回当前服务接口类型
     */
    Class<T> getInterface();

    /**
     * 返回注册中心 URL 信息
     */
    URL getUrl();
}