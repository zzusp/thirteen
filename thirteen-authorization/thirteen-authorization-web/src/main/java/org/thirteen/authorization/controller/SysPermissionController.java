package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseRecordController;
import org.thirteen.authorization.model.vo.SysPermissionVO;
import org.thirteen.authorization.service.SysPermissionService;

/**
 * @author Aaron.Sun
 * @description 权限信息接口
 * @date Created in 21:38 2020/2/21
 * @modified By
 */
@Api(tags = "权限信息接口")
@RestController
@RequestMapping(value = "/sys-permission")
public class SysPermissionController extends BaseRecordController<SysPermissionVO, SysPermissionService> {

    @Autowired
    public SysPermissionController(SysPermissionService service) {
        super(service);
    }
}
