package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thirteen.authorization.controller.base.BaseRecordController;
import org.thirteen.authorization.model.vo.SysRoleVO;
import org.thirteen.authorization.service.SysRoleService;
import org.thirteen.authorization.web.ResponseResult;

/**
 * @author Aaron.Sun
 * @description 角色信息接口
 * @date Created in 21:51 2020/2/21
 * @modified By
 */
@Api(tags = "角色信息接口")
@RestController
@RequestMapping(value = "/sys-role")
public class SysRoleController extends BaseRecordController<SysRoleVO, SysRoleService> {

    @Autowired
    public SysRoleController(SysRoleService service) {
        super(service);
    }

    @ApiOperation(value = "获取角色详细信息", notes = "由角色ID获取角色信息包含拥有的模块及权限",
        response = ResponseResult.class)
    @GetMapping(value = "/findDetailById")
    public ResponseResult<SysRoleVO> findDetailById(
        @ApiParam(required = true, value = "角色ID") @RequestParam("id") String id) {
        return ResponseResult.ok(this.service.findDetailById(id));
    }

    @ApiOperation(value = "授权", notes = "角色授权", response = ResponseResult.class)
    @PostMapping(value = "/authorize")
    public ResponseResult<String> authorize(
        @ApiParam(required = true, value = "角色信息（包含应用权限信息，角色编码不可为空）") @RequestBody SysRoleVO model) {
        this.service.authorize(model);
        return ResponseResult.ok();
    }
}
