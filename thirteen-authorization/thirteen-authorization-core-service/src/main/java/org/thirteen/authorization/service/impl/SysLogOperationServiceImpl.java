package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysLogOperationPO;
import org.thirteen.authorization.model.vo.SysLogOperationVO;
import org.thirteen.authorization.repository.SysLogOperationRepository;
import org.thirteen.authorization.service.SysLogOperationService;
import org.thirteen.authorization.service.impl.base.BaseDeleteServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 操作日志模块接口实现类
 * @date Created in 13:49 2020/2/22
 * @modified By
 */
@Service
public class SysLogOperationServiceImpl
    extends BaseDeleteServiceImpl<SysLogOperationVO, SysLogOperationPO, SysLogOperationRepository>
    implements SysLogOperationService {

    @Autowired
    public SysLogOperationServiceImpl(SysLogOperationRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
