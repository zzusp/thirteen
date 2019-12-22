package org.thirteen.authorization.common.utils;

/**
 * @author Aaron.Sun
 * @description 字符串工具类
 * @date Created in 2018/9/30 17:24
 * @modified by
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {

    /**
     * 首字母大写
     *
     * @param name 英文字符串
     * @return 首字母大写的英文字符串
     */
    public static String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }

}
