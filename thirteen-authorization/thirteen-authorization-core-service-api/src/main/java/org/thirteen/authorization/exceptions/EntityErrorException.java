package org.thirteen.authorization.exceptions;

/**
 * @author Aaron.Sun
 * @description 数据操作异常-实体类异常
 * @date Created in 22:13 2019/12/19
 * @modified by
 */
public class EntityErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityErrorException(String message) {
        super(message);
    }

    public EntityErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
