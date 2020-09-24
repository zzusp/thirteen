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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载asm动态生成class时，目标class的邻类
     *
     * @param neighbor 目标class的邻类
     * @throws IOException io异常
     */
    public void loadNeighbor(Class<?> neighbor) throws IOException {
        String className = neighbor.getName().replaceAll("\\.", "/") + ".class";
        try (InputStream input = ClassLoader.getSystemResourceAsStream(className)) {
            if (input == null) {
                return;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            this.defineClass(neighbor.getName(), output.toByteArray(), 0, output.toByteArray().length);
        }
    }
}
