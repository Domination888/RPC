package com.domination.protocol;

import com.domination.common.Invocation;
import com.domination.register.LocalRegister;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerHandler {
    public void handler(HttpServletRequest req, HttpServletResponse resp){
        //处理请求
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject() ;
            String interfaceName = invocation.getInterfaceName();

            Class classImpl = LocalRegister.get(interfaceName, "1.0");
            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            String result = (String) method.invoke(classImpl.newInstance(), invocation.getParameters());

            IOUtils.write(result,resp.getOutputStream());
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
