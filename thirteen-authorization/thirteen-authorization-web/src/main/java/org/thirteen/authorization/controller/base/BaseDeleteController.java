package org.thirteen.authorization.controller.base;

import org.thirteen.authorization.model.vo.base.BaseDeleteVO;
import org.thirteen.authorization.service.base.BaseDeleteService;

/**
 * @author Aaron.Sun
 * @description 通用控制器
 * @date Created in 20:13 2020/1/15
 * @modified by
 */
public abstract class BaseDeleteController<VO extends BaseDeleteVO, S extends BaseDeleteService<VO>>
    extends BaseController<VO, S> {

    public BaseDeleteController(S service) {
        super(service);
    }

}
