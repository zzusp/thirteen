package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseDeleteController;
import org.thirteen.authorization.model.vo.SysLogOperationVO;
import org.thirteen.authorization.service.SysLogOperationService;

/**
 * @author Aaron.Sun
 * @description 操作日志信息接口
 * @date Created in 13:53 2020/2/22
 * @modified By
 */
@Api(tags = "操作日志信息接口")
@RestController
@RequestMapping(value = "/sys-log-operation")
public class SysLogOperationController extends BaseDeleteController<SysLogOperationVO, SysLogOperationService> {

    @Autowired
    public SysLogOperationController(SysLogOperationService service) {
        super(service);
    }
}
