package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.po.SysDeptPO;
import org.thirteen.authorization.model.po.SysDeptRolePO;
import org.thirteen.authorization.model.vo.SysDeptVO;
import org.thirteen.authorization.repository.SysDeptRepository;
import org.thirteen.authorization.repository.SysDeptRoleRepository;
import org.thirteen.authorization.service.SysDeptService;
import org.thirteen.authorization.service.SysRoleService;
import org.thirteen.authorization.service.impl.base.BaseTreeSortServiceImpl;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description 部门模块接口实现类
 * @date Created in 21:15 2020/2/21
 * @modified By
 */
@Service
public class SysDeptServiceImpl extends BaseTreeSortServiceImpl<SysDeptVO, SysDeptPO, SysDeptRepository>
    implements SysDeptService {

    private final SysDeptRoleRepository sysDeptRoleRepository;
    private final SysRoleService sysRoleService;

    @Autowired
    public SysDeptServiceImpl(SysDeptRepository baseRepository, DozerMapper dozerMapper, EntityManager em,
                              SysDeptRoleRepository sysDeptRoleRepository, SysRoleService sysRoleService) {
        super(baseRepository, dozerMapper, em);
        this.sysDeptRoleRepository = sysDeptRoleRepository;
        this.sysRoleService = sysRoleService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(SysDeptVO model) {
        // 添加部门角色关联
        this.addDeptRole(model);
        super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(SysDeptVO model) {
        // 删除部门角色关联
        this.removeAllRelation(model.getId());
        // 添加部门角色关联
        this.addDeptRole(model);
        super.update(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // 删除所有部门关联
        this.removeAllRelation(id);
        super.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        // 删除所有部门关联
        ids.forEach(this::removeAllRelation);
        super.deleteInBatch(ids);
    }

    @Override
    public SysDeptVO findById(String id) {
        // 由ID查询部门信息（包含部门下的角色信息）
        return this.queryAllRoleCascaded(super.findById(id));
    }

    /**
     * 添加部门角色关联（方法名add开头，与insert区分开，沿用调用方法的事务）
     *
     * @param model 部门信息
     */
    private void addDeptRole(SysDeptVO model) throws BusinessException {
        // 判断部门角色是否为空
        if (model.getRoles() != null && model.getRoles().size() > 0) {
            // 获取所有关联集合
            List<SysDeptRolePO> deptRoleList = model.getRoles().stream()
                .map(role -> new SysDeptRolePO(model.getCode(), role.getCode())).collect(Collectors.toList());
            try {
                // 新增所有关联
                this.sysDeptRoleRepository.saveAll(deptRoleList);
            } catch (Exception e) {
                throw new BusinessException("新增部门角色关联失败", e.getCause());
            }
        }
    }

    /**
     * 删除所有部门关联（方法名remove开头，与delete区分开，沿用调用方法的事务）
     *
     * @param deptId 部门ID
     */
    private void removeAllRelation(String deptId) throws BusinessException {
        try {
            Optional<SysDeptPO> optional = this.baseRepository.findById(deptId);
            // 删除所有关联
            optional.ifPresent(model -> this.sysDeptRoleRepository.deleteByDeptCode(model.getCode()));
        } catch (Exception e) {
            throw new BusinessException("删除所有部门关联失败", e.getCause());
        }
    }

    /**
     * 由部门信息级联查询部门下的角色信息，并返回（方法名query开头，与find区分开）
     *
     * @param model 部门信息
     * @return 部门信息（包含部门下的角色信息）
     */
    private SysDeptVO queryAllRoleCascaded(SysDeptVO model) {
        if (model != null) {
            // 获取所有与部门关联的角色编码
            List<String> roleCodes = this.sysDeptRoleRepository.findAllByDeptCode(model.getCode()).stream()
                .map(SysDeptRolePO::getRoleCode).collect(Collectors.toList());
            if (roleCodes.size() > 0) {
                // 获取部门下的角色信息
                model.setRoles(this.sysRoleService.findAllByCodes(roleCodes).getList());
            }
        }
        return model;
    }

}
