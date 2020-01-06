package org.thirteen.authorization.service.impl.base;

import org.thirteen.authorization.model.po.base.BaseDeletePO;
import org.thirteen.authorization.model.vo.base.BaseDeleteVO;
import org.thirteen.authorization.service.base.BaseDeleteService;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口实现类（实体类包含删除标记信息）
 * @date Created in 15:23 2018/1/11
 * @modified by
 */
public class BaseDeleteServiceImpl<VO extends BaseDeleteVO, PO extends BaseDeletePO> extends BaseServiceImpl
    implements BaseDeleteService<VO> {

}
