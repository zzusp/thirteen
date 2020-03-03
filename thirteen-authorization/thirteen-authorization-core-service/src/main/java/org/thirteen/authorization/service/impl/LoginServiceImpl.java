package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.common.utils.JwtUtil;
import org.thirteen.authorization.common.utils.Md5Util;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.po.SysUserPO;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.repository.SysUserRepository;
import org.thirteen.authorization.service.LoginService;
import org.thirteen.authorization.service.SysUserService;

import static org.thirteen.authorization.constant.GlobalConstants.ACTIVE_ON;

/**
 * @author Aaron.Sun
 * @description 登录模块接口实现类
 * @date Created in 14:15 2020/2/23
 * @modified By
 */
@Service
public class LoginServiceImpl implements LoginService {

    /** 登录后分发token的过期时间，单位毫秒，默认1小时 */
    private final static long SIGN_EXPIRE = 60 * 60 * 1000;
    private final SysUserService sysUserService;
    private final SysUserRepository sysUserRepository;

    @Autowired
    public LoginServiceImpl(SysUserService sysUserService, SysUserRepository sysUserRepository) {
        this.sysUserService = sysUserService;
        this.sysUserRepository = sysUserRepository;
    }

    /**
     * 登录
     *
     * @param account  用户账号
     * @param password 用户密码
     */
    @Override
    public String login(String account, String password) {
        // 校验账号密码
        SysUserPO user = this.sysUserRepository.findByAccount(account);
        if (null == user) {
            throw new BusinessException("账号不存在");
        }
        if (!ACTIVE_ON.equals(user.getActive())) {
            throw new BusinessException("账号已被冻结，请联系管理员");
        }
        if (!Md5Util.encrypt(user.getAccount(), password, user.getSalt()).equals(user.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        // 分配token
        return JwtUtil.sign(account, SIGN_EXPIRE);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    @Override
    public SysUserVO getCurrentUser() {
        return this.sysUserService.findDetailByAccount(this.sysUserService.getCurrentAccount());
    }
}
