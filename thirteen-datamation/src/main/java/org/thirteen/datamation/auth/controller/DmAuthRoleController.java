package org.thirteen.datamation.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.datamation.auth.service.DmAuthRoleService;
import org.thirteen.datamation.web.ResponseResult;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化配置平台-角色模块接口
 * @date Created in 11:11 2021/2/20
 * @modified By
 */
@Api(tags = "数据化配置平台-角色模块接口")
@RestController
@RequestMapping(value = "/dmAuthRole")
public class DmAuthRoleController {

    private final DmAuthRoleService dmAuthRoleService;

    public DmAuthRoleController(DmAuthRoleService dmAuthRoleService) {
        this.dmAuthRoleService = dmAuthRoleService;
    }

    @ApiOperation(value = "角色授权", notes = "角色授权", response = ResponseResult.class)
    @PostMapping(value = "/authorize")
    public ResponseResult<String> authorize(
        @ApiParam(required = true, value = "角色授权信息") @RequestBody Map<String, Object> model) {
        this.dmAuthRoleService.authorize(model);
        return ResponseResult.ok();
    }
}
