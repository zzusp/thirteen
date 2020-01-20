package org.thirteen.authorization.controller.base;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thirteen.authorization.model.vo.base.BaseTreeSortVO;
import org.thirteen.authorization.service.base.BaseTreeSortService;
import org.thirteen.authorization.web.ResponseResult;

/**
 * @author Aaron.Sun
 * @description 通用控制器
 * @date Created in 20:13 2020/1/15
 * @modified by
 */
public abstract class BaseTreeSortController<VO extends BaseTreeSortVO, S extends BaseTreeSortService<VO>>
    extends BaseRecordController<VO, S> {

    public BaseTreeSortController(S service) {
        super(service);
    }

    @ApiOperation(value = "通过编码获取上级节点信息", notes = "通过编码获取上级节点信息",
        response = ResponseResult.class)
    @RequestMapping(value = "/findParent", method = RequestMethod.GET)
    public ResponseResult findParent(@ApiParam(required = true, value = "编码") @RequestParam("code") String code) {
        return ResponseResult.ok(this.service.findParent(code));
    }

    @ApiOperation(value = "通过编码获取所有上级节点信息", notes = "通过编码获取所有上级节点信息",
        response = ResponseResult.class)
    @RequestMapping(value = "/findAllParent", method = RequestMethod.GET)
    public ResponseResult findAllParent(@ApiParam(required = true, value = "编码") @RequestParam("code") String code) {
        return ResponseResult.ok(this.service.findAllParent(code));
    }

    @ApiOperation(value = "通过编码获取所有下级节点信息",
        notes = "通过编码获取所有下级节点信息（不包括传入节点信息）", response = ResponseResult.class)
    @RequestMapping(value = "/findAllChildren", method = RequestMethod.GET)
    public ResponseResult findAllChildren(@ApiParam(required = true, value = "编码") @RequestParam("code") String code) {
        return ResponseResult.ok(this.service.findAllChildren(code));
    }
}
