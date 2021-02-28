package org.thirteen.datamation.auth.service;

import org.springframework.web.multipart.MultipartFile;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 用户模块服务
 * @date Created in 17:33 2021/2/13
 * @modified by
 */
public interface DmAuthUserService {

    /**
     * 新增用户信息
     *
     * @param dmAuthInsert 用户信息新增对象
     */
    void insert(DmAuthInsert dmAuthInsert);

    /**
     * 根据账号获取用户详细信息（部门、组织、角色、权限、应用等）
     *
     * @param account 账号
     * @return 用户详细信息（部门、组织、角色、权限、应用等）
     */
    Map<String, Object> getDetailByAccount(String account);

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像
     */
    void uploadAvatar(MultipartFile avatar);

    /**
     * 个人信息修改
     *
     * @param model 个人信息
     */
    void profileSetting(Map<String, Object> model);

    /**
     * 密码修改
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirm     密码确认
     */
    void passwordEdit(String oldPassword, String newPassword, String confirm);

}
