package com.domination;

public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        try {
            Thread.sleep(500); // 模拟服务端耗时处理
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, " + name;
    }
}