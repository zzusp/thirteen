package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysGroupPO;
import org.thirteen.authorization.model.vo.SysGroupVO;
import org.thirteen.authorization.repository.SysGroupRepository;
import org.thirteen.authorization.service.SysGroupService;
import org.thirteen.authorization.service.impl.base.BaseTreeSortServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 组织模块接口实现类
 * @date Created in 21:24 2020/2/21
 * @modified By
 */
@Service
public class SysGroupServiceImpl
    extends BaseTreeSortServiceImpl<SysGroupVO, SysGroupPO, SysGroupRepository>
    implements SysGroupService {

    @Autowired
    public SysGroupServiceImpl(SysGroupRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
