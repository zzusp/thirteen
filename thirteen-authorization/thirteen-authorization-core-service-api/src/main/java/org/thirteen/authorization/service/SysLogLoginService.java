package org.thirteen.authorization.service;

import org.thirteen.authorization.model.vo.SysLogLoginVO;
import org.thirteen.authorization.model.vo.base.ChartVO;
import org.thirteen.authorization.service.base.BaseDeleteService;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 登录日志模块接口
 * @date Created in 13:47 2020/2/22
 * @modified By
 */
public interface SysLogLoginService extends BaseDeleteService<SysLogLoginVO> {

    /**
     * 获取访问量，即登陆次数
     *
     * @param type      时间维度 0：日 1：月 2：年
     * @param timePoint 指定的时间日期
     * @return 访问量
     */
    ChartVO<Integer> getVisits(String type, String timePoint);

    /**
     * 获取访问来源分布，即登陆地区分布
     *
     * @return 访问来源分布
     */
    ChartVO<Map<String, Object>> getDistribution();

}
