package com.domination;

import com.alibaba.nacos.api.exception.NacosException;

import com.domination.config.ReferenceConfig;
import com.domination.protocol.netty.FutureRegistry;
import com.domination.protocol.netty.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class Consumer {
    public static void main(String[] args) throws InterruptedException, NacosException {
        try {
            // 异步调用测试
            ReferenceConfig<HelloService> helloRef = new ReferenceConfig<>(HelloService.class, "HelloServiceImpl2");
            HelloService helloService = helloRef.getProxy("sayHello", true); // 异步调用

            // 异步触发（内部分配了 requestId）
            helloService.sayHello("world");
            // 从当前上下文中获取 requestId
            String requestId = RpcContext.getContext().getRequestId();
            // 等待 Future 完成（从全局注册表中获取）
            CompletableFuture<Object> future = FutureRegistry.get(requestId);
            future.thenAccept(res -> System.out.println("异步结果：" + res))
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
            System.out.println("异步调用未阻塞");

            // 同步调用测试
            ReferenceConfig<PeopleService> peopleRef = new ReferenceConfig<>(PeopleService.class, "PeopleServiceImpl");
            PeopleService peopleService = peopleRef.getProxy("createPerson",false);
            /*
            * 因为是实现类级别的代理，所以同一实现类的不同方法若要用不同的配置要创建新的PeopleService，如：
            * PeopleService peopleService = peopleRef.getProxy("setAge",false);
            */
            String id = peopleService.createPerson("张三", 18);
            System.out.println("创建对象ID：" + id);
            peopleService.setAge(id, 25);
            int age = peopleService.getAge(id);
            System.out.println("当前年龄：" + age); // 输出 25
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}