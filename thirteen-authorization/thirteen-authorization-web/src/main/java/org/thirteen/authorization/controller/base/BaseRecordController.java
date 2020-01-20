package org.thirteen.authorization.controller.base;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thirteen.authorization.model.vo.base.BaseRecordVO;
import org.thirteen.authorization.service.base.BaseRecordService;
import org.thirteen.authorization.web.ResponseResult;

/**
 * @author Aaron.Sun
 * @description 通用控制器
 * @date Created in 20:13 2020/1/15
 * @modified by
 */
public abstract class BaseRecordController<VO extends BaseRecordVO, S extends BaseRecordService<VO>> extends BaseDeleteController<VO, S> {

    public BaseRecordController(S service) {
        super(service);
    }

    @ApiOperation(value = "检查编码是否已存在", notes = "检查编码是否已存在，存在返回true，不存在返回false",
        response = ResponseResult.class)
    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)
    public ResponseResult checkCode(@ApiParam(required = true, value = "编码") @RequestParam("code") String code) {
        return ResponseResult.ok(this.service.checkCode(code));
    }

    @ApiOperation(value = "由编码获取详细信息", notes = "由编码获取详细信息", response = ResponseResult.class)
    @RequestMapping(value = "/findByCode", method = RequestMethod.GET)
    public ResponseResult findByCode(@ApiParam(required = true, value = "编码") @RequestParam("code") String code) {
        return ResponseResult.ok(this.service.findByCode(code));
    }

}
