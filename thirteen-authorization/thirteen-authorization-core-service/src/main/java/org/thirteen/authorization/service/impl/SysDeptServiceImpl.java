package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysDeptPO;
import org.thirteen.authorization.model.vo.SysDeptVO;
import org.thirteen.authorization.repository.SysDeptRepository;
import org.thirteen.authorization.service.SysDeptService;
import org.thirteen.authorization.service.impl.base.BaseTreeSortServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 部门模块接口实现类
 * @date Created in 21:15 2020/2/21
 * @modified By
 */
@Service
public class SysDeptServiceImpl extends BaseTreeSortServiceImpl<SysDeptVO, SysDeptPO, SysDeptRepository>
    implements SysDeptService {

    @Autowired
    public SysDeptServiceImpl(SysDeptRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
