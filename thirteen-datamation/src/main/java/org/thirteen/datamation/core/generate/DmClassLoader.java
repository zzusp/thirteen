package org.thirteen.datamation.core.generate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Aaron.Sun
 * @description 自定义类加载器，便于热加载
 * @date Created in 9:51 2020/9/24
 * @modified By
 */
public class DmClassLoader extends ClassLoader {

    public DmClassLoader(Class<?>... neighbors) {
        if (neighbors != null && neighbors.length > 0) {
            for (Class<?> neighbor : neighbors) {
                try {
                    loadNeighbor(neighbor);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载asm动态生成class时，目标class的邻类
     *
     * @param neighbor 目标class的邻类
     * @return 类
     * @throws ClassNotFoundException 类未找到异常
     */
    public Class<?> loadNeighbor(Class<?> neighbor) throws ClassNotFoundException {
        String className = neighbor.getName().replaceAll("\\.", "/") + ".class";
        try (InputStream input = neighbor.getClassLoader().getResourceAsStream(className)) {
            if (input == null) {
                throw new ClassNotFoundException(className);
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[input.available()];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return this.defineClass(neighbor.getName(), output.toByteArray(), 0, output.toByteArray().length);
        } catch (IOException e) {
            throw new ClassNotFoundException(className);
        }
    }

    /**
     * 重写loadClass方法，解决打包后找不到class的问题
     *
     * @param name 类名
     * @return 类
     * @throws ClassNotFoundException 类未找到异常
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream input = this.getClass().getResourceAsStream(fileName);
            if (input == null) {
                return DmClassLoader.class.getClassLoader().loadClass(name);
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[input.available()];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return this.defineClass(name, output.toByteArray(), 0, output.toByteArray().length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }
}
