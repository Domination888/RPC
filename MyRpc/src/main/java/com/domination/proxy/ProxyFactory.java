package com.domination.proxy;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.domination.common.Invocation;
import com.domination.protocol.TcpClient;
import com.domination.util.NacosServiceDiscovery;
import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class ProxyFactory {
    // 同步调用
    public static <T> T getSyncProxy(Class<T> interfaceClass, String implName, String version) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                (proxy, method, args) -> {
                    Invocation invocation = new Invocation(
                            interfaceClass.getName(),
                            implName,
                            version,
                            method.getName(),
                            method.getParameterTypes(),
                            args,
                            null // 同步调用，不需要 callbackUrl
                    );

                    Instance instance = NacosServiceDiscovery.getOneInstance(interfaceClass.getName());
                    String ip = instance.getIp();
                    int port = instance.getPort();
                    Class<?> returnType = method.getReturnType();

                    TcpClient client = new TcpClient();
                    return client.sendSync(ip, port, invocation, returnType); // 使用同步 send 方法
                });
    }
    // 异步调用
    public static <T> T getAsyncProxy(Class<T> interfaceClass, String implName, String version) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                (proxy, method, args) -> {
                    Invocation invocation = new Invocation(
                            interfaceClass.getName(),
                            implName,
                            version,
                            method.getName(),
                            method.getParameterTypes(),
                            args,
                            "tcp://127.0.0.1:9999" // 异步调用需要 callbackUrl
                    );

                    Instance instance = NacosServiceDiscovery.getOneInstance(interfaceClass.getName());
                    String ip = instance.getIp();
                    int port = instance.getPort();

                    TcpClient client = new TcpClient();
                    client.sendAsync(ip, port, invocation); // 异步发送

                    return null;
                });
    }
}