package com.domination.filter.filter;

import com.domination.common.Invocation;

public interface InvokeFilter {
    Object invoke(Invocation invocation, FilterChain chain) throws Exception;
}