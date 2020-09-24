package org.thirteen.datamation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aaron.Sun
 * @description 字符串工具类
 * @date Created in 2018/9/30 17:24
 * @modified by
 */
@SuppressWarnings("squid:S2176")
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    /**
     * 将包含下划线的字符串转为驼峰命名（首字母大写）
     *
     * @param str 包含下划线的字符串
     * @return 驼峰命名
     */
    public static String lineToHumpAndStartWithCapitalize(String str) {
        char[] chars = lineToHump(str).toCharArray();
        // 如果为小写字母，则将ascii编码前移32位
        char a = 'a';
        char z = 'z';
        if (chars[0] >= a && chars[0] <= z) {
            chars[0] = (char) (chars[0] - 32);
        }
        return String.valueOf(chars);
    }

    /**
     * 将包含下划线的字符串转为驼峰命名
     *
     * @param str 包含下划线的字符串
     * @return 驼峰命名
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
