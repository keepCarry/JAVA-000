package com.leopo.week1.question2;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException {

        try {
            Class clazz = new HelloClassLoader().findClass("Hello");
            Object obj = clazz.newInstance();
            Method method = clazz.getMethod("hello");
            method.invoke(obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            byte[] bytes = decode("G:\\学习\\github_repo\\Java_Learn_Pro\\src\\main\\java\\com\\leopo\\week1\\question2\\Hello.xlass");
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected byte[] decode(String str) throws IOException {

        byte[] results = inputStream2ByteArray(str);
        byte a = (byte) 255;
        for (int index = 0; index < results.length; index++) {
            results[index] = (byte) (a - results[index]);
        }
        return results;
    }

    protected byte[] inputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
        return data;
    }

    protected byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
