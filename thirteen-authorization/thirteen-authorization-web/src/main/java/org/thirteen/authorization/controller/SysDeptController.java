package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseTreeSortController;
import org.thirteen.authorization.model.vo.SysDeptVO;
import org.thirteen.authorization.service.SysDeptService;

/**
 * @author Aaron.Sun
 * @description 部门信息接口
 * @date Created in 21:19 2020/2/21
 * @modified By
 */
@Api(tags = "部门信息接口")
@RestController
@RequestMapping(value = "/sys-dept")
public class SysDeptController extends BaseTreeSortController<SysDeptVO, SysDeptService> {

    @Autowired
    public SysDeptController(SysDeptService service) {
        super(service);
    }
}
