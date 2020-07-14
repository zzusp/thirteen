package org.thirteen.authorization.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysLogLoginPO;
import org.thirteen.authorization.repository.base.BaseRepository;

import java.util.List;
import java.util.Map;

import static org.thirteen.authorization.model.po.base.BaseDeletePO.DEL_FLAG_NORMAL;

/**
 * @author Aaron.Sun
 * @description 登录日志数据操作层接口
 * @date Created in 13:45 2020/2/22
 * @modified By
 */
@Repository
public interface SysLogLoginRepository extends BaseRepository<SysLogLoginPO, String> {

    /**
     * 由日期获取所有访问量统计，查询范围为当天
     *
     * @param date 指定日期
     * @return 访问量统计，查询范围为当天
     */
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(value = "SELECT DATE_FORMAT(login_time, '%k') AS time, COUNT(id) AS num" +
        " FROM sys_log_login" +
        " WHERE DATE_FORMAT(login_time, '%Y%c%e')=DATE_FORMAT(?1, '%Y%c%e')" +
        " AND del_flag=" + DEL_FLAG_NORMAL +
        " GROUP BY time" +
        " ORDER BY cast(time as UNSIGNED INTEGER)", nativeQuery = true)
    List<Map<String, Object>> findAllVisitofDayByDate(String date);

    /**
     * 由日期获取所有访问量统计，查询范围为当月
     *
     * @param date 指定日期
     * @return 访问量统计，查询范围为当月
     */
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(value = "SELECT DATE_FORMAT(login_time, '%e') AS time, COUNT(id) AS num" +
        " FROM sys_log_login" +
        " WHERE DATE_FORMAT(login_time, '%Y%c')=DATE_FORMAT(?1, '%Y%c')" +
        " AND del_flag=" + DEL_FLAG_NORMAL +
        " GROUP BY time" +
        " ORDER BY cast(time as UNSIGNED INTEGER)", nativeQuery = true)
    List<Map<String, Object>> findAllVisitofMonthByDate(String date);

    /**
     * 由日期获取所有访问量统计，查询范围为当年
     *
     * @param date 指定日期
     * @return 访问量统计，查询范围为当年
     */
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(value = "SELECT DATE_FORMAT(login_time, '%c') AS time, COUNT(id) AS num" +
        " FROM sys_log_login" +
        " WHERE DATE_FORMAT(login_time, '%Y')=DATE_FORMAT(?1, '%Y')" +
        " AND del_flag=" + DEL_FLAG_NORMAL +
        " GROUP BY time" +
        " ORDER BY cast(time as UNSIGNED INTEGER)", nativeQuery = true)
    List<Map<String, Object>> findAllVisitofYearByDate(String date);

    /**
     * 获取访问来源分布，即登陆地区分布
     *
     * @return 访问来源分布，即登陆地区分布
     */
    @Query(value = "SELECT city 'name', COUNT(id) 'value'" +
        " FROM sys_log_login" +
        " WHERE del_flag=" + DEL_FLAG_NORMAL +
        " GROUP BY city", nativeQuery = true)
    List<Map<String, Object>> findAllDistribution();
}
