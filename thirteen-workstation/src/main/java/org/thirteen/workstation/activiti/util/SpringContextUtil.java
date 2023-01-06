package org.thirteen.workstation.activiti.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 孙鹏
 * @description spring上下文工具类
 * @date Created in 18:14 2022/11/28
 * @modified By
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * 设置上下文
     *
     * @param applicationContext 上下文对象
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Holder.setInstance(applicationContext);
    }

    /** 获取applicationContext */
    public static ApplicationContext getApplicationContext() {
        return Holder.getInstance();
    }

    /** 通过name获取Bean */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /** 通过class获取Bean */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /** 通过name,以及Clazz返回指定的Bean */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 利用静态内部类
     */
    private static class Holder {
        private static ApplicationContext instance;

        public static ApplicationContext getInstance() {
            return instance;
        }

        public static void setInstance(ApplicationContext instance) {
            Holder.instance = instance;
        }
    }
}
