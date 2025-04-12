package com.domination.protocol;

import com.domination.common.Invocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TcpClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T sendSync(String ip, int port, Invocation invocation, Class<T> returnType) {
        try (Socket socket = new Socket(ip, port);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

            String json = objectMapper.writeValueAsString(invocation);// JSON 序列化
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();

            outputStream.write(length); // 写长度
            outputStream.write(data);   // 写数据
            outputStream.flush();

            // 读取响应
            byte[] respLenBytes = inputStream.readNBytes(4);
            int respLen = ByteBuffer.wrap(respLenBytes).getInt();
            byte[] respBytes = inputStream.readNBytes(respLen);
            String jsonResponse = new String(respBytes, StandardCharsets.UTF_8);

            return objectMapper.readValue(jsonResponse, returnType); // 按实际类型反序列化

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAsync(String ip, int port, Invocation invocation) {
        try (Socket socket = new Socket(ip, port);
             OutputStream outputStream = socket.getOutputStream()) {

            String json = objectMapper.writeValueAsString(invocation);// JSON 序列化
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();

            outputStream.write(length); // 写长度
            outputStream.write(data);   // 写数据
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}