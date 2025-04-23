package com.domination.cluster;

import com.domination.common.Invocation;
import com.domination.common.Result;


import java.net.URL;

public interface Invoker<T> {
    default URL getUrl() {
        return null;
    }

    default Class<T> getInterface() {
        return null;
    }

    Result invoke(Invocation invocation) throws Exception;
}