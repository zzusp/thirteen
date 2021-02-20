package org.thirteen.datamation.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.thirteen.datamation.auth.service.DmLoginService;
import org.thirteen.datamation.web.ResponseResult;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化配置平台-登录接口
 * @date Created in 17:11 2021/2/13
 * @modified By
 */
@Api(tags = "数据化配置平台-登录接口")
@RestController
@RequestMapping(value = "/dmLogin")
public class DmLoginController {

    private final DmLoginService dmLoginService;

    public DmLoginController(DmLoginService dmLoginService) {
        this.dmLoginService = dmLoginService;
    }

    @ApiOperation(value = "登录", notes = "用户登录，登录成功分发token", response = ResponseResult.class)
    @PostMapping(value = "/login")
    public ResponseResult<String> login(
            @ApiParam(required = true, value = "用户账号") @RequestParam("account") String account,
            @ApiParam(required = true, value = "用户密码") @RequestParam("password") String password) {
        return ResponseResult.ok(this.dmLoginService.login(account, password));
    }

    @ApiOperation(value = "登出", notes = "用户登出", response = ResponseResult.class)
    @PostMapping(value = "/logout")
    public ResponseResult<String> logout() {
        return ResponseResult.ok();
    }

    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息", response = ResponseResult.class)
    @GetMapping(value = "/me")
    public ResponseResult<Map<String, Object>> me() {
        return ResponseResult.ok(this.dmLoginService.me());
    }
}
