package com.domination;
import com.domination.monitor.Heartbeat;
import com.domination.monitor.MethodCounter;
import com.domination.protocol.TcpServer;
import com.domination.register.LocalRegister;
import com.domination.register.NacosServiceRegister;

//public class Provider {
//    public static void main(String[] args) {
//
//        // 本地注册多个实现类
//        LocalRegister.regist(HelloService.class.getName(), "1.0", HelloServiceImpl.class);
//        LocalRegister.regist(HelloService.class.getName(), "2.0", HelloServiceImpl.class);
//        LocalRegister.regist(GoodByeService.class.getName(), "1.0", GoodByeServiceImpl.class);
//        String host = "192.168.31.11";
//        int port = 8080;
//
//        // 注册逻辑放在 lambda 中传入
//        Runnable onStarted = () -> {
//            try {
//                NacosServiceRegister.register(HelloService.class.getName(), host, port);
//                NacosServiceRegister.register(GoodByeService.class.getName(), host, port);
//
//                //  启动心跳任务
//                Heartbeat.start(HelloService.class.getName(), host, port);
//                Heartbeat.start(GoodByeService.class.getName(), host, port);
//
//                //  启动调用统计定时打印线程
//                new Thread(() -> {
//                    while (true) {
//                        try {
//                            Thread.sleep(5000);
//                            System.out.println("\n[MethodMonitor] 当前方法调用情况：");
//                            MethodCounter.printAll();
//                        } catch (InterruptedException e) {
//                            System.err.println("调用统计线程被中断！");
//                            break;
//                        }
//                    }
//                }, "Method-Monitor-Thread").start();
//
//                System.out.println("注册到 Nacos 成功！");
//            } catch (Exception e) {
//                System.err.println("注册失败：" + e.getMessage());
//                e.printStackTrace();
//            }
//        };
//
//        // 启动 HTTP Server，注册逻辑将会在 Tomcat 成功启动后执行
//        HttpServer httpServer = new HttpServer();
//        //这里用回调是因为tomcat为保证运行会卡在tomcat.getServer().await();要先启动tomcat，再注册，再持续等待
//        httpServer.start(host, port, onStarted);
//    }
//}
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
        /*
        *  。。。。。。
        * 同一接口，同一实现类的不同版本没想出来啥好的热修改方式，感觉就直接本地注册多个不同的实现类会更好点
        * 就比如这样
        * LocalRegister.regist(HelloService.class.getName(), "HelloServiceImpl:1.0", HelloServiceImpl1.0.class);
        * LocalRegister.regist(HelloService.class.getName(), "HelloServiceImpl:2.0", HelloServiceImpl2.0.class);
        * */
        try {
            // 向 Nacos 注册服务
            NacosServiceRegister.register(HelloService.class.getName(), host, port);
            NacosServiceRegister.register(PeopleService.class.getName(), host, port);
            System.out.println("注册到 Nacos 成功");

            // 启动心跳任务
            Heartbeat.start(HelloService.class.getName(), host, port);
            Heartbeat.start(PeopleService.class.getName(), host, port);

            // 启动调用统计打印线程
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        //当前方法调用情况
                        MethodCounter.printAll();
                    } catch (InterruptedException e) {
                        System.err.println("调用统计线程被中断！");
                        break;
                    }
                }
            }, "Method-Monitor-Thread").start();

            // 启动 TCP 服务监听
            TcpServer tcpServer = new TcpServer();
            tcpServer.start(port);

        } catch (Exception e) {
            System.err.println("注册或启动失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}