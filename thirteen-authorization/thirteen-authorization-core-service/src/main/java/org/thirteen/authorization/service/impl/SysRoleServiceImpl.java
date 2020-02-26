package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.po.SysRolePO;
import org.thirteen.authorization.model.vo.SysRoleVO;
import org.thirteen.authorization.repository.*;
import org.thirteen.authorization.service.SysRoleService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Aaron.Sun
 * @description 角色模块接口实现类
 * @date Created in 21:46 2020/2/21
 * @modified By
 */
@Service
public class SysRoleServiceImpl extends BaseRecordServiceImpl<SysRoleVO, SysRolePO, SysRoleRepository>
    implements SysRoleService {

    private final SysDeptRoleRepository sysDeptRoleRepository;
    private final SysRoleApplicationRepository sysRoleApplicationRepository;
    private final SysRolePermissionRepository sysRolePermissionRepository;
    private final SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    public SysRoleServiceImpl(SysRoleRepository baseRepository, DozerMapper dozerMapper, EntityManager em,
                              SysDeptRoleRepository sysDeptRoleRepository,
                              SysRoleApplicationRepository sysRoleApplicationRepository,
                              SysRolePermissionRepository sysRolePermissionRepository,
                              SysUserRoleRepository sysUserRoleRepository) {
        super(baseRepository, dozerMapper, em);
        this.sysDeptRoleRepository = sysDeptRoleRepository;
        this.sysRoleApplicationRepository = sysRoleApplicationRepository;
        this.sysRolePermissionRepository = sysRolePermissionRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // 删除所有角色的关联
        this.removeAllRelation(id);
        super.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        // 删除所有角色的关联
        ids.forEach(this::removeAllRelation);
        super.deleteInBatch(ids);
    }

    /**
     * 删除所有角色的关联（方法名remove开头，与delete区分开，沿用调用方法的事务）
     * 关联：部门角色关联，角色应用关联，角色权限关联，用户角色关联
     *
     * @param roleId 角色ID
     */
    private void removeAllRelation(String roleId) throws BusinessException {
        try {
            Optional<SysRolePO> optional = this.baseRepository.findById(roleId);
            // 删除所有关联
            optional.ifPresent(model -> {
                this.sysDeptRoleRepository.deleteByRoleCode(model.getCode());
                this.sysRoleApplicationRepository.deleteByRoleCode(model.getCode());
                this.sysRolePermissionRepository.deleteByRoleCode(model.getCode());
                this.sysUserRoleRepository.deleteByRoleCode(model.getCode());
            });
        } catch (Exception e) {
            throw new BusinessException("删除所有角色关联失败", e.getCause());
        }
    }
}
