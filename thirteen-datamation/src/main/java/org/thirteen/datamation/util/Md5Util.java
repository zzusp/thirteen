package org.thirteen.datamation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Aaron.Sun
 * @description MD5加密工具类
 * @date Created in 15:37 2017/9/18
 * @modified by
 */
public class Md5Util {

    private static final Logger logger = LoggerFactory.getLogger(Md5Util.class);
    private static MessageDigest digest = null;

    /**
     * 对口令字符串使用MD5进行转换,并返回加密后的字符串。
     *
     * @param str 口令
     * @return String 加密字符串
     */
    public static synchronized String md5(String str) {
        if (str == null) {
            return "";
        }
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
                logger.error("Failed to load the MD5 MessageDigest. " + "Epx will be unable to function normally.", nsae);
            }
        }
        // 计算哈希
        digest.update(str.getBytes());
        return toHex(digest.digest());
    }

    /**
     * 将字节数组转化为十六进制字符串。
     *
     * @param hash byte[] 一组需要转换成十六进制的字节数组
     * @return String 处理后的十六进制字符串
     */
    private static String toHex(byte[] hash) {
        StringBuilder buf = new StringBuilder(hash.length * 2);
        for (byte aHash : hash) {
            if (((int) aHash & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) aHash & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * 自定义密码加密方法
     *
     * @param username 用户名
     * @param password 密码
     * @param salt     盐
     * @return
     */
    public static String encrypt(String username, String password, String salt) {
        return Md5Util.md5(password + username + salt);
    }

}
