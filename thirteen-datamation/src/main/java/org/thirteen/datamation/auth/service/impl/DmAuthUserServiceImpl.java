package org.thirteen.datamation.auth.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thirteen.datamation.auth.constant.DmAuthCodes;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.criteria.DmAuthRule;
import org.thirteen.datamation.auth.criteria.DmAuthUpdate;
import org.thirteen.datamation.auth.service.DmAuthService;
import org.thirteen.datamation.auth.service.DmAuthUserService;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmLookup;
import org.thirteen.datamation.core.criteria.DmSort;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.core.exception.ParamErrorException;
import org.thirteen.datamation.exception.BusinessException;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.MapUtil;
import org.thirteen.datamation.util.Md5Util;
import org.thirteen.datamation.util.RandomUtil;
import org.thirteen.datamation.web.PagerResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description 用户模块服务实现
 * @date Created in 17:33 2021/2/13
 * @modified by
 */
@Service
public class DmAuthUserServiceImpl implements DmAuthUserService {

    @Value("${upload.path}")
    private String uploadPath;

    private final DmService dmService;
    private final DmAuthService dmAuthService;

    public DmAuthUserServiceImpl(DmService dmService, DmAuthService dmAuthService) {
        this.dmService = dmService;
        this.dmAuthService = dmAuthService;
    }

    @Override
    public void insert(DmAuthInsert dmAuthInsert) {
        Map<String, Object> user = dmAuthInsert.getModel();
        String salt = RandomUtil.randomChar(DmAuthCodes.SALT_LENGTH);
        String password = Md5Util.encrypt(user.get(DmAuthCodes.ACCOUNT).toString(), DmAuthCodes.DEFAULT_PASSWORD, salt);
        // 设置盐
        user.put(DmAuthCodes.SALT, salt);
        // 设置密码
        user.put(DmAuthCodes.PASSWORD, password);
        user.put(DmAuthCodes.CREATE_BY, this.dmAuthService.getCurrentAccount());
        user.put(DmAuthCodes.CREATE_TIME, LocalDateTime.now());
        // 新增参数对象
        dmAuthInsert.setModel(user);
        this.dmAuthService.insert(dmAuthInsert);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getDetailByAccount(String account) {
        DmSpecification specification = DmSpecification.of(DmAuthCodes.AUTH_USER)
                .add(DmCriteria.equal(DmAuthCodes.ACCOUNT, account))
                .add(DmLookup.of(DmAuthCodes.AUTH_DEPT).localField(DmAuthCodes.DEPT_CODE)
                        .foreignField(DmAuthCodes.CODE).as(DmAuthCodes.DEPT_KEY).unwind(true))
                .add(DmLookup.of(DmAuthCodes.AUTH_GROUP).localField(DmAuthCodes.GROUP_CODE)
                        .foreignField(DmAuthCodes.CODE).as(DmAuthCodes.GROUP_KEY).unwind(true))
                .add(DmLookup.of(DmAuthCodes.AUTH_USER_ROLE).localField(DmAuthCodes.ACCOUNT)
                        .foreignField(DmAuthCodes.ACCOUNT).as(DmAuthCodes.ROLE_KEY));
        // 获取用户信息
        Map<String, Object> user = this.dmService.findOneBySpecification(specification);
        if (user != null) {
            List<String> roleCodes = new ArrayList<>();
            // 查询部门关联的角色
            PagerResult<Map<String, Object>> deptRolePage = this.dmService.findAllBySpecification(
                    DmSpecification.of(DmAuthCodes.AUTH_DEPT_ROLE)
                            .add(DmCriteria.equal(DmAuthCodes.DEPT_CODE, user.get(DmAuthCodes.DEPT_CODE))));
            if (!deptRolePage.isEmpty()) {
                roleCodes.addAll(deptRolePage.getList().stream().map(v -> v.get(DmAuthCodes.ROLE_CODE).toString())
                        .collect(Collectors.toList()));
            }
            // 查询用户角色、权限、应用
            List<Map<String, Object>> userRoles = (List<Map<String, Object>>) user.get(DmAuthCodes.ROLE_KEY);
            if (CollectionUtils.isNotEmpty(userRoles)) {
                roleCodes.addAll(userRoles.stream().map(v -> v.get(DmAuthCodes.ROLE_CODE).toString())
                        .collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(roleCodes)) {
                DmSpecification roleSpecification;
                // 判断是否拥有超管角色
                if (roleCodes.contains(DmAuthCodes.ADMIN_CODE)) {
                    roleSpecification = DmSpecification.of(DmAuthCodes.AUTH_ROLE)
                            .add(DmCriteria.in(DmAuthCodes.CODE, roleCodes));
                    // 查询关联角色信息
                    PagerResult<Map<String, Object>> rolePage = this.dmService.findAllBySpecification(roleSpecification);
                    user.put(DmAuthCodes.ROLE_KEY, rolePage.getList());
                    // 查询所有应用、权限
                    user.put(DmAuthCodes.APP_KEY, this.dmService.findAllBySpecification(
                            DmSpecification.of(DmAuthCodes.AUTH_APP).add(DmSort.asc(DmAuthCodes.ORDER_NUM))).getList());
                    user.put(DmAuthCodes.PERMISSION_KEY, this.dmService.findAll(DmAuthCodes.AUTH_PERMISSION).getList());
                } else {
                    roleSpecification = DmSpecification.of(DmAuthCodes.AUTH_ROLE)
                            .add(DmCriteria.in(DmAuthCodes.CODE, roleCodes))
                            .add(DmLookup.of(DmAuthCodes.AUTH_ROLE_APP).localField(DmAuthCodes.CODE)
                                    .foreignField(DmAuthCodes.ROLE_CODE).as(DmAuthCodes.APP_KEY))
                            .add(DmLookup.of(DmAuthCodes.AUTH_ROLE_PERMISSION).localField(DmAuthCodes.CODE)
                                    .foreignField(DmAuthCodes.ROLE_CODE).as(DmAuthCodes.PERMISSION_KEY));
                    // 查询关联角色信息
                    PagerResult<Map<String, Object>> rolePage = this.dmService.findAllBySpecification(roleSpecification);
                    if (CollectionUtils.isNotEmpty(rolePage.getList())) {
                        List<Map<String, Object>> roleApps = new ArrayList<>();
                        List<Map<String, Object>> rolePermissions = new ArrayList<>();
                        for (Map<String, Object> role : rolePage.getList()) {
                            roleApps.addAll((List<Map<String, Object>>) role.get(DmAuthCodes.APP_KEY));
                            rolePermissions.addAll((List<Map<String, Object>>) role.get(DmAuthCodes.PERMISSION_KEY));
                        }
                        if (CollectionUtils.isNotEmpty(roleApps)) {
                            // 查询关联应用信息
                            PagerResult<Map<String, Object>> appPage = this.dmService.findAllBySpecification(
                                    DmSpecification.of(DmAuthCodes.AUTH_APP).add(DmCriteria.in(DmAuthCodes.CODE,
                                            roleApps.stream().map(v -> v.get(DmAuthCodes.APP_CODE).toString())
                                                    .collect(Collectors.toList()))).add(DmSort.asc(DmAuthCodes.ORDER_NUM)));
                            user.put(DmAuthCodes.APP_KEY, appPage.getList());
                        }
                        if (CollectionUtils.isNotEmpty(rolePermissions)) {
                            // 查询关联权限信息
                            PagerResult<Map<String, Object>> permissionPage = this.dmService.findAllBySpecification(
                                    DmSpecification.of(DmAuthCodes.AUTH_PERMISSION).add(DmCriteria.in(DmAuthCodes.CODE,
                                            rolePermissions.stream().map(v -> MapUtil.getStringValue(v, DmAuthCodes.PERMISSION_CODE))
                                                    .collect(Collectors.toList()))));
                            user.put(DmAuthCodes.PERMISSION_KEY, permissionPage.getList());
                        }
                        user.put(DmAuthCodes.ROLE_KEY, rolePage.getList());
                    }
                }
            }
        }
        return user;
    }

    @Override
    public void uploadAvatar(MultipartFile avatar) {
        if (avatar.isEmpty()) {
            throw new ParamErrorException("请选择头像！");
        }
        String account = this.dmAuthService.getCurrentAccount();
        Map<String, Object> user = this.dmService.findOneBySpecification(DmSpecification.of(DmAuthCodes.AUTH_USER)
                .add(DmCriteria.equal(DmAuthCodes.ACCOUNT, account)));
        if (user == null) {
            throw new BusinessException("用户已被删除或不存在");
        }
        // 获取文件名
        String fileName = avatar.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = null;
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        // 获取分隔符（不同系统不同）
        String sep = System.getProperty("file.separator");
        // 文件路径 上传地址+账号+分隔符+日期时间+4为随机数+后缀名
        String filePath = uploadPath + account + sep
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + RandomUtil.randomChar(4) + suffixName;
        File dest = new File(filePath);
        // 检测是否存在目录，如果不存在则创建，创建失败抛出异常
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new BusinessException(String.format("目录不存在：%s", uploadPath + account));
        }
        try {
            //上传
            avatar.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException("上传失败！", e);
        }
        // 更新用户头像路径
        user.put(DmAuthCodes.PHOTO, filePath);
        this.dmAuthService.update(DmAuthUpdate.of(DmAuthCodes.AUTH_USER).model(user)
                .rule(DmAuthRule.of().addCurrentAccount(DmAuthCodes.UPDATE_BY).addCurrentDateTime(DmAuthCodes.UPDATE_TIME)));
    }

    @Override
    public void profileSetting(Map<String, Object> model) {
        String account = this.dmAuthService.getCurrentAccount();
        Map<String, Object> user = this.dmService.findOneBySpecification(DmSpecification.of(DmAuthCodes.AUTH_USER)
                .add(DmCriteria.equal(DmAuthCodes.ACCOUNT, account)));
        user.put(DmAuthCodes.ACCOUNT, model.get(DmAuthCodes.ACCOUNT));
        user.put(DmAuthCodes.EMAIL, model.get(DmAuthCodes.EMAIL));
        user.put(DmAuthCodes.MOBILE, model.get(DmAuthCodes.MOBILE));
        user.put(DmAuthCodes.REMARK, model.get(DmAuthCodes.REMARK));
        this.dmAuthService.update(DmAuthUpdate.of(DmAuthCodes.AUTH_USER).model(user)
                .rule(DmAuthRule.of().addCurrentAccount(DmAuthCodes.UPDATE_BY).addCurrentDateTime(DmAuthCodes.UPDATE_TIME)));
    }

    @Override
    public void passwordEdit(String oldPassword, String newPassword, String confirm) {
        String account = this.dmAuthService.getCurrentAccount();
        Map<String, Object> user = this.dmService.findOneBySpecification(DmSpecification.of(DmAuthCodes.AUTH_USER)
                .add(DmCriteria.equal(DmAuthCodes.ACCOUNT, account)));
        String salt = MapUtil.getStringValue(user, DmAuthCodes.SALT);
        // 判断旧密码是否正确
        if (Md5Util.encrypt(account, oldPassword, salt).equals(user.get(DmAuthCodes.PASSWORD))) {
            // 确认密码
            if (!newPassword.equals(confirm)) {
                throw new BusinessException("密码不一致！");
            }
            // 更新盐
            salt = RandomUtil.randomChar(DmAuthCodes.SALT_LENGTH);
            // 设置盐
            user.put(DmAuthCodes.SALT, salt);
            // 设置密码
            user.put(DmAuthCodes.PASSWORD, Md5Util.encrypt(account, newPassword, salt));
            this.dmAuthService.update(DmAuthUpdate.of(DmAuthCodes.AUTH_USER).model(user)
                    .rule(DmAuthRule.of().addCurrentAccount(DmAuthCodes.UPDATE_BY).addCurrentDateTime(DmAuthCodes.UPDATE_TIME)));
        } else {
            throw new BusinessException("旧密码错误！");
        }
    }
}
