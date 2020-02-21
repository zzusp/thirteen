package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseRecordController;
import org.thirteen.authorization.model.vo.SysRoleVO;
import org.thirteen.authorization.service.SysRoleService;

/**
 * @author Aaron.Sun
 * @description 角色信息接口
 * @date Created in 21:51 2020/2/21
 * @modified By
 */
@Api(tags = "角色信息接口")
@RestController
@RequestMapping(value = "/sys-role")
public class SysRoleController extends BaseRecordController<SysRoleVO, SysRoleService> {

    @Autowired
    public SysRoleController(SysRoleService service) {
        super(service);
    }
}
