package com.domination;

import com.domination.monitor.Heartbeat;
import com.domination.monitor.MethodStatsReporter;
import com.domination.protocol.netty.NettyServer;
import com.domination.register.LocalRegister;
import com.domination.register.NacosServiceRegister;

public class Provider {
    public static void main(String[] args) {
        //这里设置的是provider的ip
        String host = "192.168.31.11";
        int port = 8080;

        // 注册本地服务实现,前面是接口名，后面是实现类的名字
        //同一接口，同一实现类的不同版本（虽然版本不一样，但其实访问的都是一个方法）
        LocalRegister.regist(HelloService.class.getName(), "HelloServiceImpl:1.0", HelloServiceImpl.class);
        LocalRegister.regist(HelloService.class.getName(), "HelloServiceImpl:2.0", HelloServiceImpl.class);
        //同一接口，不同实现类
        LocalRegister.regist(HelloService.class.getName(), "HelloServiceImpl2:1.0", HelloServiceImpl2.class);
        LocalRegister.regist(HelloService.class.getName(), "HelloServiceImpl3:1.0", HelloServiceImpl3.class);
        //不同接口
        LocalRegister.regist(PeopleService.class.getName(), "PeopleServiceImpl:1.0", PeopleServiceImpl.class);

        try {
            // 向 Nacos 注册服务
            NacosServiceRegister.register(HelloService.class.getName(), host, port);
            NacosServiceRegister.register(PeopleService.class.getName(), host, port);
            System.out.println("注册到 Nacos 成功");

            // 启动心跳任务（已修改为只创建一个线程来完成心跳重注册）
            Heartbeat.start(HelloService.class.getName(), host, port);
            Heartbeat.start(PeopleService.class.getName(), host, port);

            MethodStatsReporter.startMonitoring(); // 启动监控打印

            // 启动 TCP 服务监听
            NettyServer.start(port);

        } catch (Exception e) {
            System.err.println("注册或启动失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}