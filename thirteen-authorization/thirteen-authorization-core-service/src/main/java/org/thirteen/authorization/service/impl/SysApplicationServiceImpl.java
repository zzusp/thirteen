package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysApplicationPO;
import org.thirteen.authorization.model.vo.SysApplicationVO;
import org.thirteen.authorization.repository.SysApplicationRepository;
import org.thirteen.authorization.service.SysApplicationService;
import org.thirteen.authorization.service.impl.base.BaseTreeSortServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 应用模块接口实现类
 * @date Created in 10:43 2018/9/14
 * @modified by
 */
@Service
public class SysApplicationServiceImpl
    extends BaseTreeSortServiceImpl<SysApplicationVO, SysApplicationPO, SysApplicationRepository>
    implements SysApplicationService {

    @Autowired
    public SysApplicationServiceImpl(SysApplicationRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }
}
