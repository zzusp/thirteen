package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseRecordController;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.service.SysUserService;

/**
 * @author Aaron.Sun
 * @description 用户信息接口
 * @date Created in 21:31 2020/2/21
 * @modified By
 */
@Api(tags = "用户信息接口")
@RestController
@RequestMapping(value = "/sys-user")
public class SysUserController extends BaseRecordController<SysUserVO, SysUserService> {

    @Autowired
    public SysUserController(SysUserService service) {
        super(service);
    }
}
