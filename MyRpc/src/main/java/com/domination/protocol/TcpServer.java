package com.domination.protocol;

import com.domination.common.Invocation;
import com.domination.monitor.MethodCounter;
import com.domination.register.LocalRegister;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
//TCP 服务端，负责接收客户端请求（通过 JSON 格式），执行本地实现类方法，并根据是否异步选择回调或返回结果。
public class TcpServer {
    // Jackson 的 JSON 工具类，用于序列化 / 反序列化
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("TCP Server 正在监听端口" + port);

        while (true) {
            Socket socket = serverSocket.accept(); // / 阻塞等待客户端连接
            new Thread(() -> handleClient(socket)).start(); // 每个连接开启一个线程处理
        }
    }

    private void handleClient(Socket socket) {
        try (
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream()
        ) {
            // 接收 JSON 字符串
            byte[] lengthBytes = inputStream.readNBytes(4); // 读取长度头
            int len = ByteBuffer.wrap(lengthBytes).getInt();
            byte[] jsonBytes = inputStream.readNBytes(len); // 读取完整 JSON
            String json = new String(jsonBytes, StandardCharsets.UTF_8);

            // JSON 反序列化为 Invocation 对象
            Invocation invocation = objectMapper.readValue(json, Invocation.class);
            String interfaceName = invocation.getInterfaceName();
            String methodName = invocation.getMethodName();
            String key = invocation.getImplName() + ":" + invocation.getVersion();
            // 记录方法调用次数（用于监控）
            MethodCounter.record(interfaceName + ":" + invocation.getVersion(), methodName);
            // 获取本地实现类（从 LocalRegister 中根据接口 + 实现名 + 版本号）
            Class<?> implClass = LocalRegister.get(interfaceName, key);
            if (implClass == null) {
                System.err.println("找不到实现类：" + key);
                return;
            }
            // 使用反射调用方法
            Method method = implClass.getMethod(methodName, invocation.getParameterTypes());
            Object resultObj = method.invoke(implClass.getDeclaredConstructor().newInstance(), invocation.getParameters());

            if (invocation.getCallbackUrl() != null) {
                //如果有回调地址，就执行异步
                // 把 resultObj 包装成 Map，再序列化
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("result", resultObj);
                String jsonResult = objectMapper.writeValueAsString(resultMap);
                new Thread(() -> doCallback(invocation.getCallbackUrl(), jsonResult)).start();
            } else {
                // 同步响应：序列化结果为 JSON 再返回
                String jsonResult = objectMapper.writeValueAsString(resultObj);
                byte[] data = jsonResult.getBytes(StandardCharsets.UTF_8);
                byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();
                outputStream.write(length); // 先写长度
                outputStream.write(data);   // 再写内容
                outputStream.flush();
            }
            System.out.println("已处理方法调用：" + methodName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 使用 TCP 回调：将结果 JSON 发送给客户端
    private void doCallback(String callbackAddress, String jsonResult) {
        String[] parts = callbackAddress.replace("tcp://", "").split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream()) {
            byte[] data = jsonResult.getBytes(StandardCharsets.UTF_8);
            byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();
            outputStream.write(length); // 先写长度
            outputStream.write(data);   // 再写内容
            outputStream.flush();
            System.out.println("已发送 TCP 回调 -> " + callbackAddress);
        } catch (Exception e) {
            System.err.println("TCP 回调失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
