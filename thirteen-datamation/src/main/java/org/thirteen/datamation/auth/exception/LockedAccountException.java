package org.thirteen.datamation.auth.exception;

/**
 * @author Aaron.Sun
 * @description 账号被锁定异常
 * @date Created in 18:20 2020/2/29
 * @modified By
 */
public class LockedAccountException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LockedAccountException() {
        super();
    }

    public LockedAccountException(String message) {
        super(message);
    }

}
