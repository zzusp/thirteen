package org.thirteen.datamation.auth.service.impl;

import org.springframework.stereotype.Service;
import org.thirteen.datamation.auth.constant.DmAuthCodes;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.service.DmAuthService;
import org.thirteen.datamation.auth.service.DmAuthUserService;
import org.thirteen.datamation.util.Md5Util;
import org.thirteen.datamation.util.RandomUtil;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 用户模块服务
 * @date Created in 17:33 2021/2/13
 * @modified by
 */
@Service
public class DmAuthUserServiceImpl implements DmAuthUserService {

    private final DmAuthService dmAuthService;

    public DmAuthUserServiceImpl(DmAuthService dmAuthService) {
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
}
