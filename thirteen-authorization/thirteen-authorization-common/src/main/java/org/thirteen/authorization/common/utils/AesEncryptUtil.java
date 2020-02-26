package org.thirteen.authorization.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Aaron.Sun
 * @description AES 128bit 加密解密工具类
 * @date Created in 13:44 2019/9/23
 * @modified by
 */
public class AesEncryptUtil {

    /** 使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！ */
    private final static String KEY = "thirteen.aes";
    private final static String IV = "thirteen.aes";
    /** 密码转换类型 */
    private final static String CIPHER_TRANSFORMATION = "AES/CBC/NoPadding";
    /** 加密算法 */
    private final static String CIPHER_ALGORITHM = "AES";

    /**
     * 加密方法
     *
     * @param data 要加密的数据
     * @param key  加密key
     * @param iv   加密iv
     * @return 加密的结果
     * @throws Exception 加密抛出的异常
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        // 算法/模式/补码方式
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        int blockSize = cipher.getBlockSize();

        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }

        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), CIPHER_ALGORITHM);
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return new Base64().encodeToString(encrypted);
    }

    /**
     * 解密方法
     *
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv   解密iv
     * @return 解密的结果
     * @throws Exception 解密抛出的异常
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        byte[] encrypted = new Base64().decode(data);
        // 创建密码器
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), CIPHER_ALGORITHM);
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] original = cipher.doFinal(encrypted);
        String originalString = new String(original);
        return originalString.trim();
    }

    /**
     * 使用默认的key和iv加密
     *
     * @param data 要加密的数据
     * @return 加密的结果
     * @throws Exception 加密抛出的异常
     */
    public static String encrypt(String data) throws Exception {
        return encrypt(data, KEY, IV);
    }

    /**
     * 使用默认的key和iv解密
     *
     * @param data 要解密的数据
     * @return 解密的结果
     * @throws Exception 解密抛出的异常
     */
    public static String desEncrypt(String data) throws Exception {
        return desEncrypt(data, KEY, IV);
    }

    /**
     * 字节数组转16进制的字符串
     *
     * @param bytes 字节数组
     * @return 16进制的字符串
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length);
        String sTemp;
        for (byte aByte : bytes) {
            sTemp = Integer.toHexString(0xFF & aByte);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 主函数 测试 V型知识库
     *
     * @param args 运行入参
     * @throws Exception 主函数运行时抛出的异常
     */
    public static void main(String[] args) throws Exception {
        // 加密前密码
        String password = "123456";
        // 加密后结果
        String data = encrypt(password);
        System.out.println("加密前：" + password);
        System.out.println("加密后：" + data);
        System.out.println("解密后：" + desEncrypt(data));
    }

}
