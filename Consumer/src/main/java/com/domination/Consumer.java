package com.domination;

import com.domination.proxy.ProxyFactory;

public class Consumer {

    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("domination");
        System.out.println(result);


    }
}
