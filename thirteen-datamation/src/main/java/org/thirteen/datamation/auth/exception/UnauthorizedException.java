package org.thirteen.datamation.auth.exception;

/**
 * @author Aaron.Sun
 * @description 未登录异常
 * @date Created in 14:05 2019/4/4
 * @modified by
 */
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
