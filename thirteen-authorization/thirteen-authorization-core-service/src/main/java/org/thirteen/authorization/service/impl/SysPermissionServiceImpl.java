package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysPermissionPO;
import org.thirteen.authorization.model.vo.SysPermissionVO;
import org.thirteen.authorization.repository.SysPermissionRepository;
import org.thirteen.authorization.repository.SysUserRepository;
import org.thirteen.authorization.service.SysPermissionService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 权限模块接口实现类
 * @date Created in 21:37 2020/2/21
 * @modified By
 */
@Service
public class SysPermissionServiceImpl
    extends BaseRecordServiceImpl<SysPermissionVO, SysPermissionPO, SysPermissionRepository>
    implements SysPermissionService {

    @Autowired
    public SysPermissionServiceImpl(SysPermissionRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
