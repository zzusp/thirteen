package org.thirteen.datamation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.web.PagerResult;
import org.thirteen.datamation.web.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化配置通用接口
 * @date Created in 20:51 2020/1/15
 * @modified By
 */
@Api(tags = "数据化配置通用接口")
@RestController
@RequestMapping(value = "/dm")
public class DmController {

    private final DmService dmService;

    public DmController(DmService dmService) {
        this.dmService = dmService;
    }

    @ApiOperation(value = "刷新", notes = "刷新", response = ResponseResult.class)
    @GetMapping(value = "/refresh ")
    public ResponseResult<Map<String, Object>> refresh() {
        dmService.refresh();
        return ResponseResult.ok();
    }

    @ApiOperation(value = "新增", notes = "新增", response = ResponseResult.class)
    @PostMapping(value = "/insert")
    public ResponseResult<String> insert(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "对象信息") @RequestBody Map<String, Object> model) {
        dmService.insert(tableCode, model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "批量新增", notes = "批量新增（循环新增，非一条sql语句）", response = ResponseResult.class)
    @PostMapping(value = "/insertAll")
    public ResponseResult<String> insertAll(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "对象信息集合") @RequestBody List<Map<String, Object>> models) {
        dmService.insertAll(tableCode, models);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "修改", notes = "修改", response = ResponseResult.class)
    @PostMapping(value = "/update")
    public ResponseResult<String> update(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "对象信息") @RequestBody Map<String, Object> model) {
        dmService.update(tableCode, model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "批量修改", notes = "批量修改（循环修改，非一条sql语句）", response = ResponseResult.class)
    @PostMapping(value = "/updateAll")
    public ResponseResult<String> updateAll(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "对象信息集合") @RequestBody List<Map<String, Object>> models) {
        dmService.updateAll(tableCode, models);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID删除信息", notes = "由ID删除信息", response = ResponseResult.class)
    @DeleteMapping(value = "/deleteById")
    public ResponseResult<String> deleteById(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "ID") @RequestParam("id") String id) {
        dmService.delete(tableCode, id);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID集合批量删除信息", notes = "由ID集合批量删除信息（单条sql语句）", response = ResponseResult.class)
    @DeleteMapping(value = "/deleteInBatch")
    public ResponseResult<String> deleteInBatch(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "ID集合") @RequestParam("ids") List<String> ids) {
        dmService.deleteInBatch(tableCode, ids);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID获取详细信息", notes = "由ID获取详细信息", response = ResponseResult.class)
    @GetMapping(value = "/findById")
    public ResponseResult<Map<String, Object>> getById(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "ID") @RequestParam("id") String id) {
        return ResponseResult.ok(dmService.findById(tableCode, id));
    }

    @ApiOperation(value = "由ID集合获取详细信息", notes = "由ID集合获取详细信息", response = ResponseResult.class)
    @GetMapping(value = "/findByIds")
    public ResponseResult<List<Map<String, Object>>> getById(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode,
        @ApiParam(required = true, value = "ID集合") @RequestParam("ids") List<String> ids) {
        return ResponseResult.ok(dmService.findByIds(tableCode, ids));
    }

    @ApiOperation(value = "查询所有信息", notes = "查询所有信息", response = ResponseResult.class)
    @GetMapping(value = "/findAll")
    public ResponseResult<PagerResult<Map<String, Object>>> findAll(
        @ApiParam(required = true, value = "表名") @RequestParam String tableCode) {
        return ResponseResult.ok(dmService.findAll(tableCode));
    }

    @ApiOperation(value = "由条件查询单条信息", notes = "由条件查询单条信息", response = ResponseResult.class)
    @GetMapping(value = "/findOneByParam")
    public ResponseResult<Map<String, Object>> findOneByParam(
        @ApiParam(required = true, value = "条件") @RequestBody DmSpecification specification) {
        return ResponseResult.ok(dmService.findOneBySpecification(specification));
    }

    @ApiOperation(value = "由条件查询多条信息", notes = "由条件查询多条信息", response = ResponseResult.class)
    @GetMapping(value = "/findAllByParam")
    public ResponseResult<PagerResult<Map<String, Object>>> findAllByParam(
        @ApiParam(required = true, value = "条件") @RequestBody DmSpecification specification) {
        return ResponseResult.ok(dmService.findAllBySpecification(specification));
    }

}
