package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thirteen.authorization.common.utils.DateUtil;
import org.thirteen.authorization.common.utils.Md5Util;
import org.thirteen.authorization.common.utils.RandomUtil;
import org.thirteen.authorization.constant.GlobalConstants;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.exceptions.ParamErrorException;
import org.thirteen.authorization.model.po.SysUserPO;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.repository.SysUserRepository;
import org.thirteen.authorization.service.ProfileService;
import org.thirteen.authorization.service.SysUserService;

import java.io.File;
import java.io.IOException;

/**
 * @author Aaron.Sun
 * @description 个人中心服务接口实现类
 * @date Created in 20:12 2020/3/3
 * @modified By
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${upload.path}")
    private String uploadPath;
    /** 获取分隔符（不同系统不同） */
    String sep = System.getProperty("file.separator");

    private final SysUserService sysUserService;
    private final SysUserRepository sysUserRepository;

    @Autowired
    public ProfileServiceImpl(SysUserService sysUserService, SysUserRepository sysUserRepository) {
        this.sysUserService = sysUserService;
        this.sysUserRepository = sysUserRepository;
    }

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像
     */
    @Override
    public void uploadAvatar(MultipartFile avatar) {
        if (avatar.isEmpty()) {
            throw new ParamErrorException("请选择头像！");
        }
        SysUserPO user = this.sysUserRepository.findByAccount(this.sysUserService.getCurrentAccount());
        // 获取文件名
        String fileName = avatar.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = null;
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        // 文件路径 上传地址+账号+分隔符+日期时间+4为随机数+后缀名
        String filePath = uploadPath + user.getAccount() + sep + DateUtil.getDate("yyyyMMddHHmmss")
            + RandomUtil.randomChar(4) + suffixName;
        File dest = new File(filePath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            // 如果不存在则创建，创建失败抛出异常
            if (!dest.getParentFile().mkdirs()) {
                throw new BusinessException(String.format("目录不存在：%s", uploadPath + user.getAccount()));
            }
        }
        try {
            //上传
            avatar.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException("上传失败！", e);
        }
        // 更新用户头像路径
        user.setPhoto(filePath);
        this.sysUserRepository.save(user);

    }

    /**
     * 个人信息修改
     *
     * @param model 个人信息
     */
    @Override
    public void profileSetting(SysUserVO model) {
        SysUserPO user = this.sysUserRepository.findByAccount(this.sysUserService.getCurrentAccount());
        user.setName(model.getName());
        user.setEmail(model.getEmail());
        user.setGender(model.getGender());
        user.setMobile(model.getMobile());
        user.setRemark(model.getRemark());
        this.sysUserRepository.save(user);
    }

    /**
     * 密码修改
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirm     密码确认
     */
    @Override
    public void passwordEdit(String oldPassword, String newPassword, String confirm) {
        SysUserPO user = this.sysUserRepository.findByAccount(this.sysUserService.getCurrentAccount());
        // 判断旧密码是否正确
        if (Md5Util.encrypt(user.getAccount(), oldPassword, user.getSalt()).equals(user.getPassword())) {
            // 确认密码
            if (!newPassword.equals(confirm)) {
                throw new BusinessException("密码不一致！");
            }
            // 设置盐
            user.setSalt(RandomUtil.randomChar(GlobalConstants.SALT_LENGTH));
            // 设置密码
            user.setPassword(Md5Util.encrypt(user.getAccount(), newPassword, user.getSalt()));
            this.sysUserRepository.save(user);
        } else {
            throw new BusinessException("旧密码错误！");
        }
    }
}
