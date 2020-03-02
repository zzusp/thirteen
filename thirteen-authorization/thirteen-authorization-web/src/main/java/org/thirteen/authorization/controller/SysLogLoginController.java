package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseDeleteController;
import org.thirteen.authorization.model.vo.SysLogLoginVO;
import org.thirteen.authorization.model.vo.base.ChartVO;
import org.thirteen.authorization.service.SysLogLoginService;
import org.thirteen.authorization.web.ResponseResult;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 登录日志信息接口
 * @date Created in 13:53 2020/2/22
 * @modified By
 */
@Api(tags = "登录日志信息接口")
@RestController
@RequestMapping(value = "/sys-log-login")
public class SysLogLoginController extends BaseDeleteController<SysLogLoginVO, SysLogLoginService> {

    @Autowired
    public SysLogLoginController(SysLogLoginService service) {
        super(service);
    }

    @ApiOperation(value = "获取访问量，即登陆次数", notes = "按照不同维度获取访问量，即登陆次数", response = ResponseResult.class)
    @GetMapping(value = "/getVisits")
    public ResponseResult<ChartVO<Integer>> getVisits(
        @ApiParam(required = true, value = "时间维度 0：日 1：月 2：年") @RequestParam("type") String type,
        @ApiParam(required = true, value = "指定的时间日期") @RequestParam("timePoint") String timePoint) {
        return ResponseResult.ok(this.service.getVisits(type, timePoint));
    }

    @ApiOperation(value = "获取访问来源分布，即登陆地区分布", notes = "获取访问来源分布，即登陆地区分布", response = ResponseResult.class)
    @GetMapping(value = "/getDistribution")
    public ResponseResult<ChartVO<Map<String, Object>>> listVisits() {
        return ResponseResult.ok(this.service.getDistribution());
    }
}
