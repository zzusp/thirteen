package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysUserPO;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.repository.SysUserRepository;
import org.thirteen.authorization.service.SysUserService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 用户模块接口实现类
 * @date Created in 21:29 2020/2/21
 * @modified By
 */
@Service
public class SysUserServiceImpl extends BaseRecordServiceImpl<SysUserVO, SysUserPO, SysUserRepository>
    implements SysUserService {

    @Autowired
    public SysUserServiceImpl(SysUserRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
