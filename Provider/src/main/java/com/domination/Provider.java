package com.domination;



import com.domination.common.URL;
import com.domination.protocol.HttpServer;
import com.domination.register.LocalRegister;
import com.domination.register.RemoteRegister;

import java.net.InetSocketAddress;

public class Provider {
    public static void main(String[] args) {

        LocalRegister.regist(HelloService.class.getName(), "1.0", HelloServiceImpl.class);
        LocalRegister.regist(HelloService.class.getName(), "2.0", HelloServiceImpl.class);

        //注册中心注册
        URL url = new URL("localhost", 8080);
        RemoteRegister.regist(HelloService.class.getName(), url);

        HttpServer httpserver = new HttpServer();
        httpserver.start(url.getHostname(), url.getPort());
    }
}
