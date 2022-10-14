package org.thirteen.datamation.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description Map工具类
 * @date Created in 10:36 2021/2/20
 * @modified by
 */
public class MapUtil {

    private MapUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 根据key获取map中的某个字符串类型的值
     *
     * @param param map对象
     * @param key   key
     * @return 字符串类型的值
     */
    public static String getStringValue(Map<String, Object> param, String key) {
        if (param == null) {
            return null;
        }
        if (!param.containsKey(key)) {
            return null;
        }
        if (param.get(key) != null && StringUtils.isNotBlank(param.get(key).toString())) {
            return param.get(key).toString();
        }
        return null;
    }

    /**
     * 根据key获取map中的某个整型的值
     *
     * @param param map对象
     * @param key   key
     * @return 整型的值
     */
    public static Integer getIntegerValue(Map<String, Object> param, String key) {
        if (param == null) {
            return null;
        }
        if (!param.containsKey(key)) {
            return null;
        }
        if (param.get(key) != null && StringUtils.isNotBlank(param.get(key).toString())) {
            return Integer.parseInt(param.get(key).toString());
        }
        return null;
    }

    /**
     * 根据key获取map中的某个长整型类型的值
     *
     * @param param map对象
     * @param key   key
     * @return 长整型的值
     */
    public static Long getLongValue(Map<String, Object> param, String key) {
        if (param == null) {
            return null;
        }
        if (!param.containsKey(key)) {
            return null;
        }
        if (param.get(key) != null && StringUtils.isNotBlank(param.get(key).toString())) {
            return Long.valueOf(param.get(key).toString());
        }
        return null;
    }

    /**
     * 根据key获取map中的某个单精度浮点数类型的值
     *
     * @param param map对象
     * @param key   key
     * @return 单精度浮点数的值
     */
    public static Float getFloatValue(Map<String, Object> param, String key) {
        if (param == null) {
            return null;
        }
        if (!param.containsKey(key)) {
            return null;
        }
        if (param.get(key) != null && StringUtils.isNotBlank(param.get(key).toString())) {
            return Float.valueOf(param.get(key).toString());
        }
        return null;
    }

    /**
     * 根据key获取map中的某个双精度浮点数类型的值
     *
     * @param param map对象
     * @param key   key
     * @return 双精度浮点数的值
     */
    public static Double getDoubleValue(Map<String, Object> param, String key) {
        if (param == null) {
            return null;
        }
        if (!param.containsKey(key)) {
            return null;
        }
        if (param.get(key) != null && StringUtils.isNotBlank(param.get(key).toString())) {
            return Double.valueOf(param.get(key).toString());
        }
        return null;
    }

    /**
     * 根据key获取map中的某个字节类型的值
     *
     * @param param map对象
     * @param key   key
     * @return 字节类型的值
     */
    public static Byte getByteValue(Map<String, Object> param, String key) {
        if (param == null) {
            return null;
        }
        if (!param.containsKey(key)) {
            return null;
        }
        if (param.get(key) != null && StringUtils.isNotBlank(param.get(key).toString())) {
            return Byte.valueOf(param.get(key).toString());
        }
        return null;
    }

    /**
     * 根据key获取map中的某个Map类型的值
     *
     * @param param map对象
     * @param key   key
     * @return Map类型的值
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> getMapValue(Map<String, Object> param, String key) {
        if (param == null) {
            return null;
        }
        if (!param.containsKey(key)) {
            return null;
        }
        if (param.get(key) != null) {
            return (Map<K, V>) param.get(key);
        }
        return null;
    }

    /**
     * 根据key获取map中的某个集合类型的值
     *
     * @param param map对象
     * @param key   key
     * @return 集合类型的值
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getListValue(Map<String, Object> param, String key) {
        if (param == null) {
            return new ArrayList<>();
        }
        if (!param.containsKey(key)) {
            return new ArrayList<>();
        }
        if (param.get(key) != null) {
            return (List<T>) param.get(key);
        }
        return new ArrayList<>();
    }

}
