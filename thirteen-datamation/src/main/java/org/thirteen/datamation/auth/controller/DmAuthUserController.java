package org.thirteen.datamation.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.service.DmAuthUserService;
import org.thirteen.datamation.web.ResponseResult;

/**
 * @author Aaron.Sun
 * @description 数据化配置平台-用户模块接口
 * @date Created in 17:11 2021/2/13
 * @modified By
 */
@Api(tags = "数据化配置平台-用户模块接口")
@RestController
@RequestMapping(value = "/dmAuthUser")
public class DmAuthUserController {

    private final DmAuthUserService dmAuthUserService;

    public DmAuthUserController(DmAuthUserService dmAuthUserService) {
        this.dmAuthUserService = dmAuthUserService;
    }

    @ApiOperation(value = "新增", notes = "新增", response = ResponseResult.class)
    @PostMapping(value = "/insert")
    public ResponseResult<String> insert(
        @ApiParam(required = true, value = "新增参数对象") @RequestBody DmAuthInsert dmAuthInsert) {
        this.dmAuthUserService.insert(dmAuthInsert);
        return ResponseResult.ok();
    }
}
