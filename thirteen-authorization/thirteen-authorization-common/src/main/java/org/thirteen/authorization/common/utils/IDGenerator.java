package org.thirteen.authorization.common.utils;

/**
 * @author Aaron.Sun
 * @description ID生成器
 * * Copy From: https://github.com/hs-web/hsweb-framework/blob/7d905d12b7d241da13d1ae3f9c0def506dbd1ce6/hsweb-core/src/main/java/org/hswebframework/web/id/IDGenerator.java
 * @date Created in 16:38 2018/1/11
 * @modified by Aaron.Sun
 */
public class IDGenerator {

    private static String uuid() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String id() {
        return Md5Util.md5(uuid().concat(RandomUtil.randomChar()));
    }

}