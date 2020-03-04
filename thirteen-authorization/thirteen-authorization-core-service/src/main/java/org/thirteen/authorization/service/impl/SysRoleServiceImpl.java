package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.params.base.CriteriaParam;
import org.thirteen.authorization.model.params.base.SortParam;
import org.thirteen.authorization.model.po.SysRoleApplicationPO;
import org.thirteen.authorization.model.po.SysRolePO;
import org.thirteen.authorization.model.po.SysRolePermissionPO;
import org.thirteen.authorization.model.vo.SysRoleVO;
import org.thirteen.authorization.repository.*;
import org.thirteen.authorization.service.SysApplicationService;
import org.thirteen.authorization.service.SysPermissionService;
import org.thirteen.authorization.service.SysRoleService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.thirteen.authorization.constant.GlobalConstants.ACTIVE_ON;
import static org.thirteen.authorization.constant.GlobalConstants.ADMIN_CODE;
import static org.thirteen.authorization.service.support.base.ModelInformation.CODE_FIELD;
import static org.thirteen.authorization.service.support.base.ModelInformation.SORT_FIELD;

/**
 * @author Aaron.Sun
 * @description 角色模块接口实现类
 * @date Created in 21:46 2020/2/21
 * @modified By
 */
@Service
public class SysRoleServiceImpl extends BaseRecordServiceImpl<SysRoleVO, SysRolePO, SysRoleRepository>
    implements SysRoleService {

    private final SysApplicationService sysApplicationService;
    private final SysPermissionService sysPermissionService;
    private final SysDeptRoleRepository sysDeptRoleRepository;
    private final SysRoleApplicationRepository sysRoleApplicationRepository;
    private final SysRolePermissionRepository sysRolePermissionRepository;
    private final SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    public SysRoleServiceImpl(SysRoleRepository baseRepository, DozerMapper dozerMapper, EntityManager em,
                              SysApplicationService sysApplicationService,
                              SysPermissionService sysPermissionService,
                              SysDeptRoleRepository sysDeptRoleRepository,
                              SysRoleApplicationRepository sysRoleApplicationRepository,
                              SysRolePermissionRepository sysRolePermissionRepository,
                              SysUserRoleRepository sysUserRoleRepository) {
        super(baseRepository, dozerMapper, em);
        this.sysApplicationService = sysApplicationService;
        this.sysPermissionService = sysPermissionService;
        this.sysDeptRoleRepository = sysDeptRoleRepository;
        this.sysRoleApplicationRepository = sysRoleApplicationRepository;
        this.sysRolePermissionRepository = sysRolePermissionRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // 删除所有角色的关联
        this.baseRepository.findById(id).ifPresent(item -> this.removeAllRelation(item.getCode()));
        super.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        // 删除所有角色的关联
        ids.forEach(id -> {
            this.baseRepository.findById(id).ifPresent(item -> this.removeAllRelation(item.getCode()));
        });
        super.deleteInBatch(ids);
    }

    /**
     * 删除所有角色的关联（方法名remove开头，与delete区分开，沿用调用方法的事务）
     * 关联：部门角色关联，角色应用关联，角色权限关联，用户角色关联
     *
     * @param roleCode 角色编码
     */
    private void removeAllRelation(String roleCode) throws BusinessException {
        try {
            this.sysDeptRoleRepository.deleteByRoleCode(roleCode);
            this.sysRoleApplicationRepository.deleteByRoleCode(roleCode);
            this.sysRolePermissionRepository.deleteByRoleCode(roleCode);
            this.sysUserRoleRepository.deleteByRoleCode(roleCode);
        } catch (Exception e) {
            throw new BusinessException("删除所有角色关联失败", e.getCause());
        }
    }

    @Override
    public SysRoleVO findDetailById(String id) {
        // 获取角色信息
        SysRoleVO model = this.findById(id);
        // 判断角色信息是否为null
        if (model != null) {
            BaseParam applicationParam = BaseParam.of().add(SortParam.asc(SORT_FIELD));
            BaseParam permissionParam = BaseParam.of().add(SortParam.asc(CODE_FIELD));
            // 判断角色是否为超级管理员
            if (!ADMIN_CODE.equals(model.getCode())) {
                // 获取角色应用关联
                List<SysRoleApplicationPO> roleApplications = this.sysRoleApplicationRepository
                    .findAllByRoleCode(model.getCode());
                // 如果角色应用关联不为空，添加查询条件
                if (roleApplications != null && roleApplications.size() > 0) {
                    List<String> applicationCodes = roleApplications.stream()
                        .map(SysRoleApplicationPO::getApplicationCode).collect(Collectors.toList());
                    applicationParam.add(CriteriaParam.in(CODE_FIELD, applicationCodes).and());
                    // 获取角色下的应用信息
                    model.setApplications(this.sysApplicationService.findAllByParam(applicationParam).getList());
                }
                // 获取角色权限关联
                List<SysRolePermissionPO> rolePermissions = this.sysRolePermissionRepository
                    .findAllByRoleCode(model.getCode());
                // 如果角色应用关联不为空，添加查询条件
                if (rolePermissions != null && rolePermissions.size() > 0) {
                    List<String> permissionCodes = rolePermissions.stream()
                        .map(SysRolePermissionPO::getPermissionCode).collect(Collectors.toList());
                    permissionParam.add(CriteriaParam.in(CODE_FIELD, permissionCodes).and());
                    // 获取角色下的权限信息
                    model.setPermissions(this.sysPermissionService.findAllByParam(permissionParam).getList());
                }
            } else {
                // 获取角色下的应用信息
                model.setApplications(this.sysApplicationService.findAllByParam(applicationParam).getList());
                // 获取角色下的权限信息
                model.setPermissions(this.sysPermissionService.findAllByParam(permissionParam).getList());
            }
        }
        return model;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void authorize(SysRoleVO model) {
        if (StringUtil.isEmpty(model.getCode())) {
            throw new BusinessException("角色编码不可为空！");
        }
        // 获取角色信息
        SysRoleVO role = this.findByCode(model.getCode());
        if (!ACTIVE_ON.equals(model.getActive())) {
            throw new BusinessException("角色已被禁用！");
        }
        // 判断角色信息是否为null
        if (role != null) {
            // 判断角色是否为超级管理员
            if (ADMIN_CODE.equals(role.getCode())) {
                throw new BusinessException("超级管理员拥有所有应用及权限，不可修改！");
            } else {
                // 删除原有关联
                this.removeAllRelation(role.getCode());
                // 添加新关联
                this.addRoleApplication(model);
                this.addRolePermission(model);
            }
        } else {
            throw new BusinessException("角色不存在或已删除！");
        }
    }

    /**
     * 添加角色应用关联（方法名add开头，与insert区分开，沿用调用方法的事务）
     *
     * @param model 角色信息
     */
    private void addRoleApplication(SysRoleVO model) throws BusinessException {
        // 判断角色应用是否为空
        if (model.getApplications() != null && model.getApplications().size() > 0) {
            // 获取所有关联集合
            List<SysRoleApplicationPO> roleApplicationList = model.getApplications().stream()
                .map(application -> new SysRoleApplicationPO(model.getCode(), application.getCode()))
                .collect(Collectors.toList());
            try {
                // 新增所有关联
                this.sysRoleApplicationRepository.saveAll(roleApplicationList);
            } catch (Exception e) {
                throw new BusinessException("新增角色应用关联失败", e.getCause());
            }
        }
    }

    /**
     * 添加角色权限关联（方法名add开头，与insert区分开，沿用调用方法的事务）
     *
     * @param model 角色信息
     */
    private void addRolePermission(SysRoleVO model) throws BusinessException {
        // 判断角色权限是否为空
        if (model.getPermissions() != null && model.getPermissions().size() > 0) {
            // 获取所有关联集合
            List<SysRolePermissionPO> rolePermissionList = model.getPermissions().stream()
                .map(permission -> new SysRolePermissionPO(model.getCode(), permission.getCode()))
                .collect(Collectors.toList());
            try {
                // 新增所有关联
                this.sysRolePermissionRepository.saveAll(rolePermissionList);
            } catch (Exception e) {
                throw new BusinessException("新增角色权限关联失败", e.getCause());
            }
        }
    }
}
