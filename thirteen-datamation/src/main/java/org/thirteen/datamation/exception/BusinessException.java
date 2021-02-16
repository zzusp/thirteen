package org.thirteen.datamation.exception;

/**
 * @author Aaron.Sun
 * @description 业务异常
 * @date Created in 14:46 2018/1/11
 * @modified by
 * <p>
 * 常见状态代码、状态描述、说明：
 * <p>
 * 200 OK      // 客户端请求成功
 * 400 Bad Request  // 客户端请求有语法错误，不能被服务器所理解
 * 401 Unauthorized // 请求未经授权（未登录）
 * 403 Forbidden  // 服务器收到请求，但是拒绝提供服务（权限不足）
 * 404 Not Found  // 请求资源不存在，eg：输入了错误的URL
 * 500 Internal Server Error // 服务器发生不可预期的错误（操作失败）
 * 503 Server Unavailable  // 服务器当前不能处理客户端的请求，一段时间后可能恢复正常
 * <p>
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
