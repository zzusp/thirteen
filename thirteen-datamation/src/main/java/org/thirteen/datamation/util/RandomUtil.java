package org.thirteen.datamation.util;

import java.util.Random;

/**
 * @author Aaron.Sun
 * @description 随机数工具，用于产生随机数，随机密码等
 * * Copy From: https://github.com/hs-web/hsweb-utils/blob/master/src/main/java/org/hswebframework/utils/RandomUtil.java
 * @date Created in 16:49 2018/1/11
 * @modified by
 */
public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static Random getRandom() {
        return RANDOM;
    }

    private static char[] chars = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 随机生成由0-9a-zA-Z组合而成的字符串
     *
     * @param len 字符串长度
     * @return 生成结果
     */
    public static String randomChar(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(chars[RANDOM.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    public static String randomChar() {
        return randomChar(8);
    }

    /**
     * 生成对应位数的随机字符串
     *
     * @param length 生成字符串的长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
