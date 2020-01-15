package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseTreeSortController;
import org.thirteen.authorization.model.vo.SysApplicationVO;
import org.thirteen.authorization.service.SysApplicationService;
import org.thirteen.authorization.service.base.BaseTreeSortService;

/**
 * @author Aaron.Sun
 * @description 应用信息接口
 * @date Created in 20:51 2020/1/15
 * @modified By
 */
@Api(tags = "应用信息接口")
@RestController
@RequestMapping(value = "/sys-application")
public class SysApplicationController extends BaseTreeSortController<SysApplicationVO> {

    @Autowired
    public SysApplicationController(BaseTreeSortService<SysApplicationVO> service) {
        super(service);
    }
}
