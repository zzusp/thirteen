package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseRecordController;
import org.thirteen.authorization.model.vo.SysBizTypeVO;
import org.thirteen.authorization.service.SysBizTypeService;

/**
 * @author Aaron.Sun
 * @description 业务类型信息接口
 * @date Created in 21:54 2020/2/21
 * @modified By
 */
@Api(tags = "业务类型信息接口")
@RestController
@RequestMapping(value = "/sys-biztype")
public class SysBizTypeController extends BaseRecordController<SysBizTypeVO, SysBizTypeService> {

    @Autowired
    public SysBizTypeController(SysBizTypeService service) {
        super(service);
    }
}
