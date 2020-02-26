package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.po.SysPermissionPO;
import org.thirteen.authorization.model.vo.SysPermissionVO;
import org.thirteen.authorization.repository.SysPermissionRepository;
import org.thirteen.authorization.repository.SysRolePermissionRepository;
import org.thirteen.authorization.service.SysPermissionService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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

    private final SysRolePermissionRepository sysRolePermissionRepository;

    @Autowired
    public SysPermissionServiceImpl(SysPermissionRepository baseRepository, DozerMapper dozerMapper, EntityManager em,
                                    SysRolePermissionRepository sysRolePermissionRepository) {
        super(baseRepository, dozerMapper, em);
        this.sysRolePermissionRepository = sysRolePermissionRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // 删除所有权限关联
        this.removeAllRelation(id);
        super.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        // 删除所有权限关联
        ids.forEach(this::removeAllRelation);
        super.deleteInBatch(ids);
    }

    /**
     * 删除所有权限关联（方法名remove开头，与delete区分开，沿用调用方法的事务）
     *
     * @param permissionId 权限ID
     */
    private void removeAllRelation(String permissionId) throws BusinessException {
        try {
            Optional<SysPermissionPO> optional = this.baseRepository.findById(permissionId);
            // 删除所有关联
            optional.ifPresent(model -> this.sysRolePermissionRepository.deleteByPermissionCode(model.getCode()));
        } catch (Exception e) {
            throw new BusinessException("删除所有权限关联失败", e.getCause());
        }
    }
}
