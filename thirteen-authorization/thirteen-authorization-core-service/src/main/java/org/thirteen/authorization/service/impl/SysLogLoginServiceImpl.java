package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysLogLoginPO;
import org.thirteen.authorization.model.vo.SysLogLoginVO;
import org.thirteen.authorization.repository.SysLogLoginRepository;
import org.thirteen.authorization.service.SysLogLoginService;
import org.thirteen.authorization.service.impl.base.BaseDeleteServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 登录日志模块接口实现类
 * @date Created in 13:49 2020/2/22
 * @modified By
 */
@Service
public class SysLogLoginServiceImpl extends BaseDeleteServiceImpl<SysLogLoginVO, SysLogLoginPO, SysLogLoginRepository>
    implements SysLogLoginService {

    @Autowired
    public SysLogLoginServiceImpl(SysLogLoginRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
