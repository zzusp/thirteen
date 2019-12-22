package org.thirteen.authorization.exceptions;

/**
 * @author Aaron.Sun
 * @description 数据操作异常-未找到数据异常
 * @date Created in 22:13 2019/12/19
 * @modified by
 */
public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
