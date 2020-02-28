package org.thirteen.authorization.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * @author Aaron.Sun
 * @description JSON转换工具类
 * @date Created in 10:40 2018/1/18
 * @modified by
 */
public class JsonUtil {

    /**
     * 序列化配置
     */
    private static final SerializeConfig CONFIG;

    /**
     * 默认日期格式
     */
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    static {
        CONFIG = new SerializeConfig();
        changeDateFormat(dateFormat);
    }

    private static void changeDateFormat(String dateFormat) {
        //jackson默认写出的时间数据为时间戳， 这里修改为相应模式的时间数据输出格式
        CONFIG.put(java.util.Date.class, new SimpleDateFormatSerializer(dateFormat));
        //jackson默认写出的时间数据为时间戳， 这里修改为相应模式的时间数据输出格式
        CONFIG.put(java.sql.Date.class, new SimpleDateFormatSerializer(dateFormat));
    }

    /**
     * 对象转字符串
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return JSON.toJSONString(object, CONFIG);
    }

    /**
     * 对象转字符串（带日期格式化）
     *
     * @param object
     * @param dateFormat 日期格式
     * @return
     */
    public static String toString(Object object, String dateFormat) {
        changeDateFormat(dateFormat);
        return JSON.toJSONString(object, CONFIG);
    }

    /**
     * 字符串转指定类
     *
     * @param text  字符串
     * @param clazz 指定类
     * @param <T>   泛型
     * @return
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

}
