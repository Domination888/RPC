package com.domination;

public class HelloServiceImpl2 implements HelloService {
    public String sayHello(String name) {
        try {
            Thread.sleep(3000); // 模拟服务端耗时处理
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello2, " + name;
    }
}
