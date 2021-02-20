package org.thirteen.datamation.auth.service.impl;

import org.springframework.stereotype.Service;
import org.thirteen.datamation.auth.constant.DmAuthCodes;
import org.thirteen.datamation.auth.exception.UnauthorizedException;
import org.thirteen.datamation.auth.service.DmLoginService;
import org.thirteen.datamation.auth.service.DmValidateService;
import org.thirteen.datamation.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化通用鉴权服务接口实现类
 * @date Created in 18:44 2021/2/19
 * @modified by
 */
@Service
public class DmValidateServiceImpl implements DmValidateService {

    private final DmLoginService dmLoginService;

    public DmValidateServiceImpl(DmLoginService dmLoginService) {
        this.dmLoginService = dmLoginService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean validate(String url) {
        boolean flag = false;
        // 获取用户详细信息（包含用户角色、用户权限等信息）
        Map<String, Object> user = this.dmLoginService.me();
        try {
            if (user != null) {
                List<Map<String, Object>> permissions = (List<Map<String, Object>>) user.get(DmAuthCodes.PERMISSION_KEY);
                if (CollectionUtils.isNotEmpty(permissions)) {
                    for (Map<String, Object> perm : permissions) {
                        // 判断请求路径是否与权限中的路径匹配
                        if (url.equals(perm.get(DmAuthCodes.URL))) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new UnauthorizedException("鉴权失败", e.getCause());
        }
        return flag;
    }
}
