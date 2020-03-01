package org.thirteen.authorization.aop;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thirteen.authorization.common.utils.LogUtil;
import org.thirteen.authorization.exceptions.*;
import org.thirteen.authorization.web.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;
import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description controller层统一异常处理
 * @date Created in 21:35 2018/5/30
 * @modified by
 */
@RestControllerAdvice
public class ControllerExceptionHandleAdvice {

    /**
     * 可以直接写@ExceptionHandler,不指明异常类，会自动映射
     */
    @ExceptionHandler({SignatureException.class, UnauthorizedException.class})
    public ResponseResult handlerUnauthorized() {
        return ResponseResult.unauthorized();
    }

    @ExceptionHandler(LockedAccountException.class)
    public ResponseResult handlerLockedAccount(LockedAccountException e) {
        return ResponseResult.unauthorized(e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseResult handlerForbidden() {
        return ResponseResult.forbidden();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseResult handlerNotFound(NotFoundException e) {
        return ResponseResult.notFind(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseResult handlerBusiness(BusinessException e) {
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler
    public ResponseResult handler(HttpServletResponse res, Exception e) {
        LogUtil.getLogger().info("Restful Http请求发生异常");
        if (res.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            res.setStatus(HttpStatus.OK.value());
        }
        LogUtil.getLogger().error("请求异常：", e);
        // 针对不同的异常类型，返回对应的信息
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseResult.error(e.getMessage());
        } else if (e instanceof NullPointerException) {
            return ResponseResult.error("发生空指针异常");
        } else if (e instanceof IllegalArgumentException) {
            return ResponseResult.bad("请求参数类型不匹配");
        } else if (e instanceof SQLException) {
            return ResponseResult.error("数据库访问异常");
        } else if (e instanceof DataNotFoundException) {
            return ResponseResult.error(e.getMessage());
        } else if (e instanceof ParamErrorException) {
            return ResponseResult.error(e.getMessage());
        } else if (e instanceof EntityErrorException) {
            return ResponseResult.error("实体类异常");
        } else {
            return ResponseResult.error("请求失败,请联系管理员");
        }
    }

}
