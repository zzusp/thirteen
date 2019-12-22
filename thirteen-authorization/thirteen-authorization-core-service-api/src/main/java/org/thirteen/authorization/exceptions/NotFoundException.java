package org.thirteen.authorization.exceptions;

/**
 * @author Aaron.Sun
 * @description 数据资源等未找到异常
 * @date Created in 15:09 2018/1/11
 * @modified by
 */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
