package org.thirteen.datamation.auth.service.impl;

import org.springframework.stereotype.Service;
import org.thirteen.datamation.auth.constant.DmAuthCodes;
import org.thirteen.datamation.auth.service.DmAuthRoleService;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmInsert;
import org.thirteen.datamation.core.criteria.DmLookup;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.exception.BusinessException;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.util.MapUtil;
import org.thirteen.datamation.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 角色模块服务实现
 * @date Created in 10:36 2021/2/20
 * @modified by
 */
@Service
public class DmAuthRoleServiceImpl implements DmAuthRoleService {

    private final DmService dmService;

    public DmAuthRoleServiceImpl(DmService dmService) {
        this.dmService = dmService;
    }

    @Override
    public void authorize(Map<String, Object> model) {
        String roleCode = MapUtil.getStringValue(model, DmAuthCodes.CODE);
        if (StringUtils.isEmpty(roleCode)) {
            throw new BusinessException("角色授权时，角色编码不可为空");
        }
        Map<String, Object> role = this.dmService.findOneBySpecification(DmSpecification.of(DmAuthCodes.AUTH_ROLE)
            .add(DmCriteria.equal(DmAuthCodes.CODE, roleCode)));
        if (role == null) {
            throw new BusinessException("角色授权失败，角色不存在或已被删除");
        }
        if (DmAuthCodes.ADMIN_CODE.equals(roleCode)) {
            throw new BusinessException("角色授权失败，超级管理员默认拥有所有权限，无需授权");
        } else {
            // 级联删除应用、权限关联
            this.dmService.delete(roleCode, DmLookup.of(DmAuthCodes.AUTH_ROLE_APP).foreignField(DmAuthCodes.ROLE_CODE));
            this.dmService.delete(roleCode, DmLookup.of(DmAuthCodes.AUTH_ROLE_PERMISSION).foreignField(DmAuthCodes.ROLE_CODE));
            // 保存应用、权限关联
            List<Map<String, Object>> apps = MapUtil.getListValue(model, DmAuthCodes.APP_KEY);
            if (!apps.isEmpty()) {
                apps.forEach(v -> v.put(DmAuthCodes.ROLE_CODE, roleCode));
                this.dmService.insertAll(DmInsert.of(DmAuthCodes.AUTH_ROLE_APP).models(apps));
            }
            List<Map<String, Object>> permissions = MapUtil.getListValue(model, DmAuthCodes.PERMISSION_KEY);
            if (!permissions.isEmpty()) {
                permissions.forEach(v -> v.put(DmAuthCodes.ROLE_CODE, roleCode));
                this.dmService.insertAll(DmInsert.of(DmAuthCodes.AUTH_ROLE_PERMISSION).models(permissions));
            }
        }
    }

}
