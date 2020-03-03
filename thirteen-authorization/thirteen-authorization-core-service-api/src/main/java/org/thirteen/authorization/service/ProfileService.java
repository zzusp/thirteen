package org.thirteen.authorization.service;

import org.springframework.web.multipart.MultipartFile;
import org.thirteen.authorization.model.vo.SysUserVO;

/**
 * @author Aaron.Sun
 * @description 个人中心服务接口
 * @date Created in 20:12 2020/3/3
 * @modified By
 */
public interface ProfileService {

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
    void profileSetting(SysUserVO model);

    /**
     * 密码修改
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirm     密码确认
     */
    void passwordEdit(String oldPassword, String newPassword, String confirm);

}
