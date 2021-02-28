package org.thirteen.datamation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.model.vo.DmTableVO;
import org.thirteen.datamation.service.DmTableService;
import org.thirteen.datamation.web.PagerResult;
import org.thirteen.datamation.web.ResponseResult;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 数据化表信息配置接口
 * @date Created in 16:33 2021/1/22
 * @modified By
 */
@Api(tags = "数据化表信息配置接口")
@RestController
@RequestMapping(value = "/dmTable")
public class DmTableController {

    private final DmTableService dmTableService;

    public DmTableController(DmTableService dmTableService) {
        this.dmTableService = dmTableService;
    }

    @ApiOperation(value = "检查数据是否已存在", notes = "检查数据是否已存在，存在返回true，不存在返回false",
        response = ResponseResult.class)
    @GetMapping(value = "/isExist")
    public ResponseResult<Boolean> isExist(@ApiParam(required = true, value = "编码") @RequestParam("code") String code) {
        return ResponseResult.ok(this.dmTableService.isExist(code));
    }

    @ApiOperation(value = "新增", notes = "新增", response = ResponseResult.class)
    @PostMapping(value = "/insert")
    public ResponseResult<String> insert(@ApiParam(required = true, value = "对象信息") @RequestBody DmTableVO model) {
        this.dmTableService.insert(model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "修改", notes = "修改", response = ResponseResult.class)
    @PostMapping(value = "/update")
    public ResponseResult<String> update(@ApiParam(required = true, value = "对象信息") @RequestBody DmTableVO model) {
        this.dmTableService.update(model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID删除信息", notes = "由ID删除信息", response = ResponseResult.class)
    @DeleteMapping(value = "/deleteById")
    public ResponseResult<String> deleteById(@ApiParam(required = true, value = "ID") @RequestParam("id") String id) {
        this.dmTableService.delete(id);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID集合批量删除信息", notes = "由ID集合批量删除信息", response = ResponseResult.class)
    @DeleteMapping(value = "/deleteInBatch")
    public ResponseResult<String> deleteInBatch(
        @ApiParam(required = true, value = "ID集合") @RequestParam("ids") List<String> ids) {
        this.dmTableService.deleteInBatch(ids);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID获取详细信息", notes = "由ID获取详细信息", response = ResponseResult.class)
    @GetMapping(value = "/findById")
    public ResponseResult<DmTableVO> getById(@ApiParam(required = true, value = "ID") @RequestParam("id") String id) {
        return ResponseResult.ok(this.dmTableService.findById(id));
    }

    @ApiOperation(value = "由条件查询多条信息", notes = "由条件查询多条信息", response = ResponseResult.class)
    @PostMapping(value = "/findAllBySpecification")
    public ResponseResult<PagerResult<DmTableVO>> findAllBySpecification(
        @ApiParam(required = true, value = "条件") @RequestBody DmSpecification dmSpecification) {
        return ResponseResult.ok(this.dmTableService.findAllBySpecification(dmSpecification));
    }


}
