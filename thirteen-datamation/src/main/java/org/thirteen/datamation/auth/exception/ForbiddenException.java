package org.thirteen.datamation.auth.exception;

/**
 * @author Aaron.Sun
 * @description 权限不足异常
 * @date Created in 14:05 2019/4/4
 * @modified by
 */
public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

}
