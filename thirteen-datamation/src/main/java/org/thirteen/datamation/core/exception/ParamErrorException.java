package org.thirteen.datamation.core.exception;

/**
 * @author Aaron.Sun
 * @description 查询参数异常
 * @date Created in 17:12 2020/1/1913
 * @modified by
 */
public class ParamErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParamErrorException(String message) {
        super("查询参数异常：" + message);
    }

    public ParamErrorException(String message, Throwable cause) {
        super("查询参数异常：" + message, cause);
    }

}
