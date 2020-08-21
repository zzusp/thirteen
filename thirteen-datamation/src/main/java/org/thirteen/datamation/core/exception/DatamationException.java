package org.thirteen.datamation.core.exception;

/**
 * @author Aaron.Sun
 * @description 数据化框架异常
 * @date Created in 11:27 2020/8/21
 * @modified by
 */
public class DatamationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatamationException(String message) {
        super(message);
    }

    public DatamationException(String message, Throwable cause) {
        super(message, cause);
    }

}
