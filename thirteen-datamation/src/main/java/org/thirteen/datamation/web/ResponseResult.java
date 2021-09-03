package org.thirteen.datamation.web;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 统一API响应结果封装
 * @date Created in 17:40 2018/8/22
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 4798702095576739866L;

    public static final String SUCCESS_MESSAGE = "请求成功";
    public static final String BAD_MESSAGE = "请求参数类型不匹配！";
    public static final String UNAUTHORIZED_MESSAGE = "您尚未登录或无操作时间过长，请重新登录！";
    public static final String LOCKED_MESSAGE = "账号已被冻结，请联系管理员！";
    public static final String FORBIDDEN_MESSAGE = "您没有足够的权限执行该操作！";

    /**
     * 状态码
     */
    private int status;
    /**
     * 消息
     */
    private String message;
    /**
     * 结果
     */
    private T result;

    /**
     * 请求成功
     *
     * @param <T> 泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> ok() {
        return ok(null);
    }

    /**
     * 请求成功
     *
     * @param result 结果
     * @param <T>    泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> ok(T result) {
        return new ResponseResult<T>().status(200).result(result).message(SUCCESS_MESSAGE);
    }

    /**
     * 请求失败（如：请求参数类型不匹配）
     *
     * @param message 信息
     * @param <T>     泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> bad(String message) {
        return error(400, BAD_MESSAGE);
    }

    /**
     * 请求失败（如：未登录或未通过验证）
     *
     * @param <T> 泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> unauthorized() {
        return error(401, UNAUTHORIZED_MESSAGE);
    }

    /**
     * 请求失败（如：未登录或未通过验证）
     *
     * @param message 信息
     * @param <T>     泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 请求失败（如：账号被冻结）
     *
     * @param <T> 泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> locked() {
        return error(401, LOCKED_MESSAGE);
    }

    /**
     * 请求失败（如：权限不足）
     *
     * @param <T> 泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> forbidden() {
        return error(403, FORBIDDEN_MESSAGE);
    }

    /**
     * 请求失败（如：权限不足）
     *
     * @param message 信息
     * @param <T>     泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 请求失败（如：请求资源不存在）
     *
     * @param message 信息
     * @param <T>     泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> notFind(String message) {
        return error(404, message);
    }

    /**
     * 请求失败（如：查询数据库报错）
     *
     * @param message 信息
     * @param <T>     泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> error(String message) {
        return error(500, message);
    }

    /**
     * 请求失败（如：请求频繁或系统维护）
     *
     * @param message 信息
     * @param <T>     泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> unavailable(String message) {
        return error(503, message);
    }

    /**
     * 请求失败（如：服务器异常）
     *
     * @param status  状态码
     * @param message 信息
     * @param <T>     泛型对象
     * @return 响应结果对象
     */
    public static <T> ResponseResult<T> error(int status, String message) {
        return new ResponseResult<T>().status(status).message(message);
    }

    public ResponseResult<T> status(int status) {
        this.status = status;
        return this;
    }

    public ResponseResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public ResponseResult<T> result(T result) {
        this.result = result;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
