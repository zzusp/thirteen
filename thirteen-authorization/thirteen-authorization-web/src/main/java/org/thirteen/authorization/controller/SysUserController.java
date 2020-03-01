package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseRecordController;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.service.SysUserService;
import org.thirteen.authorization.web.ResponseResult;

/**
 * @author Aaron.Sun
 * @description 用户信息接口
 * @date Created in 21:31 2020/2/21
 * @modified By
 */
@Api(tags = "用户信息接口")
@RestController
@RequestMapping(value = "/sys-user")
public class SysUserController extends BaseRecordController<SysUserVO, SysUserService> {

    @Autowired
    public SysUserController(SysUserService service) {
        super(service);
    }

    @ApiOperation(value = "检查账号是否已存在", notes = "检查账号是否已存在，存在返回true，不存在返回false",
        response = ResponseResult.class)
    @RequestMapping(value = "/checkAccount", method = RequestMethod.GET)
    public ResponseResult<Boolean> checkAccount(
        @ApiParam(required = true, value = "账号") @RequestParam("account") String account) {
        return ResponseResult.ok(this.service.checkAccount(account));
    }
}
