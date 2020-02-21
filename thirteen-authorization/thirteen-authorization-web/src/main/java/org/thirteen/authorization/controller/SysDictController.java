package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thirteen.authorization.controller.base.BaseRecordController;
import org.thirteen.authorization.model.vo.SysDictVO;
import org.thirteen.authorization.service.SysDictService;

/**
 * @author Aaron.Sun
 * @description 数据字典信息接口
 * @date Created in 21:54 2020/2/21
 * @modified By
 */
@Api(tags = "数据字典信息接口")
@RestController
@RequestMapping(value = "/sys-dict")
public class SysDictController extends BaseRecordController<SysDictVO, SysDictService> {

    @Autowired
    public SysDictController(SysDictService service) {
        super(service);
    }
}
