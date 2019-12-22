package org.thirteen.authorization.exceptions;

/**
 * @author Aaron.Sun
 * @description 数据操作异常-逻辑删除异常
 * @date Created in 22:13 2019/12/19
 * @modified by
 */
public class LogicDeleteErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LogicDeleteErrorException(String message) {
        super(message);
    }

    public LogicDeleteErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
