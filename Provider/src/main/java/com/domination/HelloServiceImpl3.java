package com.domination;

public class HelloServiceImpl3 implements HelloService {
    @Override
    public String sayHello(String name) {
        try {
            Thread.sleep(1000); // 模拟服务端耗时处理
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello3, " + name;
    }
}
