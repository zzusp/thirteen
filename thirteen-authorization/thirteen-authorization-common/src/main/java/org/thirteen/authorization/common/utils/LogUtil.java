package org.thirteen.authorization.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Aaron.Sun
 * @description 日志输出工具类
 * @date Created in 17:32 2018/1/16
 * @modified by
 */
public class LogUtil {

    private static Logger logger = null;

    public static Logger getLogger() {
        if (null == logger) {
            // 创建一个异常，从堆栈中获取调用者的类名
            String classname = new Exception().getStackTrace()[1].getClassName();
            logger = LoggerFactory.getLogger(classname);
        }
        return logger;
    }
}
