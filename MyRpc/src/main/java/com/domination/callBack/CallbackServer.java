package com.domination.callBack;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CallbackServer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void start(int port) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Callback TCP Server 已启动在端口 " + port);

                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> handleConnection(socket)).start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "callback-tcp-server-thread").start();
    }

    private static void handleConnection(Socket socket) {
        try (InputStream inputStream = socket.getInputStream()) {
//            byte[] buffer = new byte[2048];
//            int len = inputStream.read(buffer);
//            String json = new String(buffer, 0, len, StandardCharsets.UTF_8);

            byte[] lengthBytes = inputStream.readNBytes(4); // 读取长度头
            int length = ByteBuffer.wrap(lengthBytes).getInt();
            byte[] jsonBytes = inputStream.readNBytes(length); // 读取完整 JSON
            String json = new String(jsonBytes, StandardCharsets.UTF_8);

            try {
                Map<String, Object> map = objectMapper.readValue(json, Map.class);
                System.out.println("异步回调结果（JSON）：");
                map.forEach((k, v) -> System.out.println("  " + k + " : " + v));
            } catch (Exception e) {
                System.out.println("JSON 解析失败，原文如下：\n" + json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
