package com.domination.filter.clusterFilter.impl;

import com.domination.common.Activate;
import com.domination.common.Invocation;
import com.domination.filter.clusterFilter.ClusterFilterChain;
import com.domination.filter.clusterFilter.ClusterInvokeFilter;


@Activate(group = "cluster")
public class AuthClusterFilter implements ClusterInvokeFilter {
    @Override
    public Object invoke(Invocation invocation, ClusterFilterChain chain) throws Exception {
        String token = (String) invocation.getAttachments().get("authToken");
        if (!"secure-token".equals(token)) {
            throw new RuntimeException("鉴权失败：无效令牌");
        }
        return chain.invoke(invocation);
    }
}
