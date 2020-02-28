package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.Md5Util;
import org.thirteen.authorization.common.utils.RandomUtil;
import org.thirteen.authorization.constant.GlobalConstants;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.params.base.CriteriaParam;
import org.thirteen.authorization.model.po.SysDeptRolePO;
import org.thirteen.authorization.model.po.SysUserPO;
import org.thirteen.authorization.model.po.SysUserRolePO;
import org.thirteen.authorization.model.po.base.BaseRecordPO;
import org.thirteen.authorization.model.vo.SysRoleVO;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.repository.SysDeptRoleRepository;
import org.thirteen.authorization.repository.SysUserRepository;
import org.thirteen.authorization.repository.SysUserRoleRepository;
import org.thirteen.authorization.service.SysApplicationService;
import org.thirteen.authorization.service.SysPermissionService;
import org.thirteen.authorization.service.SysRoleService;
import org.thirteen.authorization.service.SysUserService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.thirteen.authorization.constant.GlobalConstants.ACTIVE_ON;
import static org.thirteen.authorization.service.support.base.ModelInformation.ACTIVE_FIELD;
import static org.thirteen.authorization.service.support.base.ModelInformation.DEL_FLAG_FIELD;

/**
 * @author Aaron.Sun
 * @description 用户模块接口实现类
 * @date Created in 21:29 2020/2/21
 * @modified By
 */
@Service
public class SysUserServiceImpl extends BaseRecordServiceImpl<SysUserVO, SysUserPO, SysUserRepository>
    implements SysUserService {

    private final SysUserRoleRepository sysUserRoleRepository;
    private final SysDeptRoleRepository sysDeptRoleRepository;
    private final SysRoleService sysRoleService;
    private final SysApplicationService sysApplicationService;
    private final SysPermissionService sysPermissionService;

    @Autowired
    public SysUserServiceImpl(SysUserRepository baseRepository, DozerMapper dozerMapper, EntityManager em,
                              SysUserRoleRepository sysUserRoleRepository, SysDeptRoleRepository sysDeptRoleRepository,
                              SysRoleService sysRoleService, SysApplicationService sysApplicationService,
                              SysPermissionService sysPermissionService) {
        super(baseRepository, dozerMapper, em);
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.sysDeptRoleRepository = sysDeptRoleRepository;
        this.sysRoleService = sysRoleService;
        this.sysApplicationService = sysApplicationService;
        this.sysPermissionService = sysPermissionService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(SysUserVO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        // 验证用户账号是否存在（不包含已逻辑删除的用户账号）
        if (this.checkAccount(model.getAccount())) {
            throw new BusinessException("用户账号已存在");
        }
        // 用户暂无编码字段
        model.setCode(null);
        // 设置默认状态为启用
        model.setActive(ACTIVE_ON);
        model.setCreateBy("");
        model.setCreateTime(LocalDateTime.now());
        // 设置盐
        model.setSalt(RandomUtil.randomChar(GlobalConstants.SALT_LENGTH));
        // 设置密码
        model.setPassword(Md5Util.encrypt(model.getAccount(), GlobalConstants.DEFAULT_PASSWORD, model.getSalt()));
        // 添加用户角色关联
        this.addUserRole(model);
        this.baseRepository.save(this.converToPo(model));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(SysUserVO model) {
        // 删除用户角色关联
        this.removeAllRelation(model.getId());
        // 添加用户角色关联
        this.addUserRole(model);
        super.update(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // 删除所有用户关联
        this.removeAllRelation(id);
        super.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        // 删除所有用户关联
        ids.forEach(this::removeAllRelation);
        super.deleteInBatch(ids);
    }

    /**
     * 检查用户账号是否已存在
     *
     * @param account 用户账号
     * @return 用户账号是否已存在
     */
    @Override
    public boolean checkAccount(String account) {
        BaseParam param = BaseParam.of().add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseRecordPO.DEL_FLAG_NORMAL).and())
            .add(CriteriaParam.equal("account", account).and());
        return this.baseRepository.findOne(this.createSpecification(param.getCriterias())).isPresent();
    }

    /**
     * 由用户账号获取用户信息详情（包含用户角色、用户权限等信息）
     *
     * @param account 用户账号
     * @return 用户信息详情（包含用户角色、用户权限等信息）
     */
    @Override
    public SysUserVO findDetailByAccount(String account) {
        // 由账号获取用户基本信息
        SysUserVO user = this.findOneByParam(BaseParam.of().add(CriteriaParam.equal("account", account).and()));
        // 判断用户信息是否为null
        if (user != null) {
            // 清空密码
            user.setPassword(null);
            // 清空盐
            user.setSalt(null);
            // 角色ID去重临时变量
            Set<String> roleCodeSet = new HashSet<>();
            // 获取所有与用户关联的角色编码
            roleCodeSet.addAll(this.sysUserRoleRepository.findAllByAccount(account).stream()
                .map(SysUserRolePO::getRoleCode).collect(Collectors.toList()));
            // 获取所有与用户所属部门关联的角色编码
            roleCodeSet.addAll(this.sysDeptRoleRepository.findAllByDeptCode(user.getDept().getCode()).stream()
                .map(SysDeptRolePO::getRoleCode).collect(Collectors.toList()));
            // 判断角色ID集合大小是否大于0
            if (roleCodeSet.size() > 0) {
                // 添加用户及用户所属部门拥有的启用角色
                user.setRoles(this.sysRoleService.findAllByCodes(new ArrayList<>(roleCodeSet)).getList());
                BaseParam param = BaseParam.of().add(CriteriaParam.equal(ACTIVE_FIELD, ACTIVE_ON).and());
                // 验证用户是否拥有超级管理员角色
                if (!checkAdmin(user)) {
                    param.add(CriteriaParam.in("roleCode", new ArrayList<>(roleCodeSet)).and());
                }
                // 获取角色下的启用应用信息
                user.setApplications(this.sysApplicationService.findAllByParam(param).getList());
                // 获取角色下的启用权限信息
                user.setPermissions(this.sysPermissionService.findAllByParam(param).getList());
            }
        }
        return user;
    }

    /**
     * 添加用户角色关联（方法名add开头，与insert区分开，沿用调用方法的事务）
     *
     * @param model 用户信息
     */
    private void addUserRole(SysUserVO model) throws BusinessException {
        // 判断用户角色是否为空
        if (model.getRoles() != null && model.getRoles().size() > 0) {
            // 获取所有关联集合
            List<SysUserRolePO> userRoleList = model.getRoles().stream()
                .map(role -> new SysUserRolePO(model.getCode(), role.getCode())).collect(Collectors.toList());
            try {
                // 新增所有关联
                this.sysUserRoleRepository.saveAll(userRoleList);
            } catch (Exception e) {
                throw new BusinessException("新增用户角色关联失败", e.getCause());
            }
        }
    }

    /**
     * 删除所有用户关联（方法名remove开头，与delete区分开，沿用调用方法的事务）
     *
     * @param userId 用户ID
     */
    private void removeAllRelation(String userId) throws BusinessException {
        try {
            Optional<SysUserPO> optional = this.baseRepository.findById(userId);
            // 删除所有关联
            optional.ifPresent(model -> this.sysUserRoleRepository.deleteByAccount(model.getCode()));
        } catch (Exception e) {
            throw new BusinessException("删除所有用户关联失败", e.getCause());
        }
    }

    /**
     * 由用户信息级联查询用户下的角色信息，并返回（方法名query开头，与find区分开）
     *
     * @param model 用户信息
     * @return 用户信息（包含用户下的角色信息）
     */
    private SysUserVO queryAllRoleCascaded(SysUserVO model) {
        if (model != null) {
            // 获取所有与用户关联的角色编码
            List<String> roleCodes = this.sysUserRoleRepository.findAllByRoleCode(model.getCode()).stream()
                .map(SysUserRolePO::getRoleCode).collect(Collectors.toList());
            if (roleCodes.size() > 0) {
                // 获取用户下的角色信息
                model.setRoles(this.sysRoleService.findAllByCodes(roleCodes).getList());
            }
        }
        return model;
    }

    /**
     * 验证用户是否拥有超级管理员角色
     *
     * @param user 用户及角色信息
     * @return 用户是否拥有超级管理员角色
     */
    private static boolean checkAdmin(SysUserVO user) {
        // 标识
        boolean flag = false;
        // 判断用户信息及用户角色信息是否为空
        if (user != null && user.getRoles() != null && user.getRoles().size() > 0) {
            // 遍历用户所有角色信息
            for (SysRoleVO role : user.getRoles()) {
                if (GlobalConstants.ADMIN_CODE.equals(role.getCode())) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
