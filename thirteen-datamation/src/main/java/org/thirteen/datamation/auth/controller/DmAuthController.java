package org.thirteen.datamation.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.criteria.DmAuthSpecification;
import org.thirteen.datamation.auth.criteria.DmAuthUpdate;
import org.thirteen.datamation.auth.service.DmAuthService;
import org.thirteen.datamation.web.PagerResult;
import org.thirteen.datamation.web.ResponseResult;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化配置通用接口-整合鉴权模块
 * @date Created in 17:11 2021/2/13
 * @modified By
 */
@Api(tags = "数据化配置通用接口-整合鉴权模块")
@RestController
@RequestMapping(value = "/dmAuth")
public class DmAuthController {

    private final DmAuthService dmAuthService;

    public DmAuthController(DmAuthService dmAuthService) {
        this.dmAuthService = dmAuthService;
    }

    @ApiOperation(value = "新增", notes = "新增", response = ResponseResult.class)
    @PostMapping(value = "/insert")
    public ResponseResult<String> insert(
        @ApiParam(required = true, value = "新增参数对象") @RequestBody DmAuthInsert dmAuthInsert) {
        dmAuthService.insert(dmAuthInsert);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "批量新增", notes = "批量新增（循环新增，非一条sql语句）", response = ResponseResult.class)
    @PostMapping(value = "/insertAll")
    public ResponseResult<String> insertAll(
        @ApiParam(required = true, value = "新增参数对象") @RequestBody DmAuthInsert dmAuthInsert) {
        dmAuthService.insertAll(dmAuthInsert);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "修改", notes = "修改", response = ResponseResult.class)
    @PostMapping(value = "/update")
    public ResponseResult<String> update(
        @ApiParam(required = true, value = "更新参数对象") @RequestBody DmAuthUpdate dmAuthUpdate) {
        dmAuthService.update(dmAuthUpdate);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "批量修改", notes = "批量修改（循环修改，非一条sql语句）", response = ResponseResult.class)
    @PostMapping(value = "/updateAll")
    public ResponseResult<String> updateAll(
        @ApiParam(required = true, value = "更新参数对象") @RequestBody DmAuthUpdate dmAuthUpdate) {
        dmAuthService.updateAll(dmAuthUpdate);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由条件查询单条信息", notes = "由条件查询单条信息", response = ResponseResult.class)
    @PostMapping(value = "/findOneByParam")
    public ResponseResult<Map<String, Object>> findOneByParam(
        @ApiParam(required = true, value = "条件") @RequestBody DmAuthSpecification specification) {
        return ResponseResult.ok(dmAuthService.findOneBySpecification(specification));
    }

    @ApiOperation(value = "由条件查询多条信息", notes = "由条件查询多条信息", response = ResponseResult.class)
    @PostMapping(value = "/findAllByParam")
    public ResponseResult<PagerResult<Map<String, Object>>> findAllByParam(
        @ApiParam(required = true, value = "条件") @RequestBody DmAuthSpecification specification) {
        return ResponseResult.ok(dmAuthService.findAllBySpecification(specification));
    }

    @ApiOperation(value = "由条件查询是否存在", notes = "由条件查询是否存在", response = ResponseResult.class)
    @PostMapping(value = "/isExist")
    public ResponseResult<Boolean> isExist(
        @ApiParam(required = true, value = "条件") @RequestBody DmAuthSpecification specification) {
        return ResponseResult.ok(dmAuthService.isExist(specification));
    }
}
