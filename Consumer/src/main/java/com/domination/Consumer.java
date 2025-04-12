package com.domination;

import com.domination.callBack.CallbackServer;
import com.domination.proxy.ProxyFactory;

import java.time.LocalTime;

public class Consumer {

    public static void main(String[] args) throws InterruptedException {
        // 启动客户端的 callback 服务监听 9999 端口
        CallbackServer.start(9999);
        Thread.sleep(1000);
        // test发起异步调用
        System.out.println("开始发送异步调用请求：" + LocalTime.now());
        HelloService helloAsync = ProxyFactory.getAsyncProxy(HelloService.class, "HelloServiceImpl", "1.0");
        helloAsync.sayHello("domination"); // 会异步发送，不阻塞
        HelloService helloAsync2 = ProxyFactory.getAsyncProxy(HelloService.class, "HelloServiceImpl2", "1.0");
        helloAsync2.sayHello("domination050319"); // 会异步发送，不阻塞
        System.out.println("所有请求已发出（不等待结果）：" + LocalTime.now());
        //test发起同步调用
        HelloService helloService = ProxyFactory.getSyncProxy(HelloService.class, "HelloServiceImpl3", "1.0");
        String result = helloService.sayHello("domination");
        System.out.println("同步返回结果：" + result);

        PeopleService peopleService = ProxyFactory.getSyncProxy(PeopleService.class, "PeopleServiceImpl", "1.0");

        String id = peopleService.createPerson("张三", 18);
        System.out.println("创建对象ID：" + id);

        peopleService.setAge(id, 25);
        int age = peopleService.getAge(id);
        System.out.println("当前年龄：" + age); // 输出 25
    }
}
