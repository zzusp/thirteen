package org.thirteen.authorization.controller.base;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.thirteen.authorization.common.utils.JsonUtil;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.vo.base.BaseVO;
import org.thirteen.authorization.service.base.BaseService;
import org.thirteen.authorization.web.PagerResult;
import org.thirteen.authorization.web.ResponseResult;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 通用控制器
 * @date Created in 20:13 2020/1/15
 * @modified by
 */
public abstract class BaseController<VO extends BaseVO, S extends BaseService<VO>> {

    /** service声明 */
    protected final S service;

    public BaseController(S service) {
        this.service = service;
    }

    @ApiOperation(value = "新增", notes = "新增", response = ResponseResult.class)
    @PostMapping(value = "/insert")
    public ResponseResult<String> insert(@ApiParam(required = true, value = "对象信息") @RequestBody VO model) {
        this.service.insert(model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "批量新增", notes = "批量新增", response = ResponseResult.class)
    @PostMapping(value = "/insertAll")
    public ResponseResult<String> insertAll(@ApiParam(required = true, value = "对象信息集合") @RequestBody List<VO> models) {
        this.service.insertAll(models);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "修改", notes = "修改", response = ResponseResult.class)
    @PostMapping(value = "/update")
    public ResponseResult<String> update(@ApiParam(required = true, value = "对象信息") @RequestBody VO model) {
        this.service.update(model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "批量修改", notes = "批量修改", response = ResponseResult.class)
    @PostMapping(value = "/updateAll")
    public ResponseResult<String> updateAll(@ApiParam(required = true, value = "对象信息集合") @RequestBody List<VO> models) {
        this.service.updateAll(models);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID删除信息", notes = "由ID删除信息", response = ResponseResult.class)
    @DeleteMapping(value = "/deleteById")
    public ResponseResult<String> deleteById(@ApiParam(required = true, value = "ID") @RequestParam("id") String id) {
        this.service.delete(id);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID集合批量删除信息", notes = "由ID集合批量删除信息", response = ResponseResult.class)
    @DeleteMapping(value = "/deleteInBatch")
    public ResponseResult<String> deleteInBatch(
        @ApiParam(required = true, value = "ID集合") @RequestParam("ids") List<String> ids) {
        this.service.deleteInBatch(ids);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "由ID获取详细信息", notes = "由ID获取详细信息", response = ResponseResult.class)
    @GetMapping(value = "/findById")
    public ResponseResult<VO> getById(@ApiParam(required = true, value = "ID") @RequestParam("id") String id) {
        return ResponseResult.ok(this.service.findById(id));
    }

    @ApiOperation(value = "由ID集合获取详细信息", notes = "由ID集合获取详细信息", response = ResponseResult.class)
    @GetMapping(value = "/findByIds")
    public ResponseResult<PagerResult<VO>> getById(@ApiParam(required = true, value = "ID集合") @RequestParam("ids") List<String> ids) {
        return ResponseResult.ok(this.service.findByIds(ids));
    }

    @ApiOperation(value = "查询所有信息", notes = "查询所有信息", response = ResponseResult.class)
    @GetMapping(value = "/findAll")
    public ResponseResult<PagerResult<VO>> findAll() {
        return ResponseResult.ok(this.service.findAll());
    }

    @ApiOperation(value = "由条件查询单条信息", notes = "由条件查询单条信息", response = ResponseResult.class)
    @GetMapping(value = "/findOneByParam")
    public ResponseResult<VO> findOneByParam(
        @ApiParam(required = true, value = "条件") @RequestParam("param") String param) {
        return ResponseResult.ok(this.service.findOneByParam(JsonUtil.parseObject(param, BaseParam.class)));
    }

    @ApiOperation(value = "由条件查询多条信息", notes = "由条件查询多条信息", response = ResponseResult.class)
    @GetMapping(value = "/findAllByParam")
    public ResponseResult<PagerResult<VO>> findAllByParam(
        @ApiParam(required = true, value = "条件") @RequestParam("param") String param) {
        return ResponseResult.ok(this.service.findAllByParam(JsonUtil.parseObject(param, BaseParam.class)));
    }
}
