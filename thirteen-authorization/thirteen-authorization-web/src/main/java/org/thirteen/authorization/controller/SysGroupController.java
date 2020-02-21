package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseTreeSortController;
import org.thirteen.authorization.model.vo.SysGroupVO;
import org.thirteen.authorization.service.SysGroupService;

/**
 * @author Aaron.Sun
 * @description 组织信息接口
 * @date Created in 21:26 2020/2/21
 * @modified By
 */
@Api(tags = "组织信息接口")
@RestController
@RequestMapping(value = "/sys-group")
public class SysGroupController extends BaseTreeSortController<SysGroupVO, SysGroupService> {

    @Autowired
    public SysGroupController(SysGroupService service) {
        super(service);
    }
}
