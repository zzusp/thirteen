package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysRolePO;
import org.thirteen.authorization.model.vo.SysRoleVO;
import org.thirteen.authorization.repository.SysRoleRepository;
import org.thirteen.authorization.service.SysRoleService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 角色模块接口实现类
 * @date Created in 21:46 2020/2/21
 * @modified By
 */
@Service
public class SysRoleServiceImpl extends BaseRecordServiceImpl<SysRoleVO, SysRolePO, SysRoleRepository>
    implements SysRoleService {

    @Autowired
    public SysRoleServiceImpl(SysRoleRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
