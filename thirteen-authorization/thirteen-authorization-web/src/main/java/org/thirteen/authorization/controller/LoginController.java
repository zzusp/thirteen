package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.service.LoginService;
import org.thirteen.authorization.web.ResponseResult;

/**
 * @author Aaron.Sun
 * @description 登录登出接口
 * @date Created in 21:35 2018/4/12
 * @modified by
 */
@Api(tags = "登录登出接口")
@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @ApiOperation(value = "登录", notes = "用户登录，登录成功分发token", response = ResponseResult.class)
    @PostMapping(value = "/login")
    public ResponseResult login(
        @ApiParam(required = true, value = "用户账号") @RequestParam("account") String account,
        @ApiParam(required = true, value = "用户密码") @RequestParam("password") String password) {
        return ResponseResult.ok(this.loginService.login(account, password));
    }

    @ApiOperation(value = "登出", notes = "用户登出", response = ResponseResult.class)
    @PostMapping(value = "/logout")
    public ResponseResult logout() {
        return ResponseResult.ok();
    }

    @ApiOperation(value = "获取当前登陆用户信息", notes = "获取当前登陆用户信息", response = ResponseResult.class)
    @GetMapping(value = "/getCurrentUser")
    public ResponseResult getCurrentUser() {
        return ResponseResult.ok(this.loginService.getCurrentUser());
    }

}