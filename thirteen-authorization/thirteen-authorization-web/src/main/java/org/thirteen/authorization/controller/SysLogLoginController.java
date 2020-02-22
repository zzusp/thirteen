package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseDeleteController;
import org.thirteen.authorization.model.vo.SysLogLoginVO;
import org.thirteen.authorization.service.SysLogLoginService;

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
}
