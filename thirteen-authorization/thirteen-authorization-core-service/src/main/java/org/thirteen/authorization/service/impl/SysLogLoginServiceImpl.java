package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.common.utils.DateUtil;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysLogLoginPO;
import org.thirteen.authorization.model.vo.SysLogLoginVO;
import org.thirteen.authorization.model.vo.base.ChartVO;
import org.thirteen.authorization.model.vo.base.SeriesVO;
import org.thirteen.authorization.repository.SysLogLoginRepository;
import org.thirteen.authorization.service.SysLogLoginService;
import org.thirteen.authorization.service.impl.base.BaseDeleteServiceImpl;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 登录日志模块接口实现类
 * @date Created in 13:49 2020/2/22
 * @modified By
 */
@Service
public class SysLogLoginServiceImpl extends BaseDeleteServiceImpl<SysLogLoginVO, SysLogLoginPO, SysLogLoginRepository>
    implements SysLogLoginService {

    public static final String VISITS_CHART_NAME = "用户访问量趋势";
    public static final String DISTRIBUTION_CHART_NAME = "访问来源分布";
    /**
     * 日期类型，日月年（按不同维度查询，判断时使用）0：日 1：月 2：年
     */
    public static final String DATETIME_TYPE_DAY = "0";
    public static final String DATETIME_TYPE_MONTH = "1";
    public static final String DATETIME_TYPE_YEAR = "2";

    @Autowired
    public SysLogLoginServiceImpl(SysLogLoginRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

    /**
     * 获取访问量，即登陆次数
     *
     * @param type      时间维度 0：日 1：月 2：年
     * @param timePoint 指定的时间日期
     * @return 访问量
     */
    @Override
    public ChartVO<Integer> getVisits(String type, String timePoint) {
        // X轴信息
        List<String> axisx = new ArrayList<>();
        // 小时数，天数，月数
        int timeSize = 0;
        // 单位
        String unit = "";
        // 判断标识
        boolean flag;
        // 获取访问量，即登陆次数
        List<Map<String, Object>> list;
        // 封装查询数据到返回对象中
        ChartVO<Integer> result = new ChartVO<>();
        result.setName(VISITS_CHART_NAME);
        result.setAxisx(axisx);
        result.setSeries(new ArrayList<>());
        SeriesVO<Integer> series = new SeriesVO<>();
        series.setData(new ArrayList<>());
        // 根据不同的时间维度生成对应的X轴，及核心数据
        switch (type) {
            case DATETIME_TYPE_DAY:
                list = this.baseRepository.findAllVisitofDayByDate(timePoint);
                timeSize = 24;
                unit = "h";
                break;
            case DATETIME_TYPE_MONTH:
                list = this.baseRepository.findAllVisitofMonthByDate(timePoint);
                timeSize = DateUtil.getDaysOfMonth(timePoint);
                unit = "号";
                break;
            case DATETIME_TYPE_YEAR:
                list = this.baseRepository.findAllVisitofYearByDate(timePoint);
                timeSize = 12;
                unit = "月";
                break;
            default:
                list = new ArrayList<>();
                break;
        }
        for (int i = 1; i <= timeSize; i++) {
            axisx.add(i + unit);
            // 判断时间是否相等，如果相等，将访问量赋给对应时间。数据库函数可能会返回0日/月，所以添加了一个条件以保证正常运行
            flag = list.size() > 0 && (0 == Integer.parseInt((String) list.get(0).get("time"))
                || i == Integer.parseInt((String) list.get(0).get("time")));
            if (flag) {
                series.getData().add(((BigInteger) list.get(0).get("num")).intValue());
                list.remove(0);
            } else {
                series.getData().add(0);
            }
        }
        result.getSeries().add(series);
        return result;
    }

    /**
     * 获取访问来源分布，即登陆地区分布
     *
     * @return 访问来源分布
     */
    @Override
    public ChartVO<Map<String, Object>> getDistribution() {
        // X轴信息
        List<String> axisx = new ArrayList<>();
        // 获取访问来源分布
        List<Map<String, Object>> list = this.baseRepository.findAllDistribution();
        // 封装查询数据到返回对象中
        ChartVO<Map<String, Object>> result = new ChartVO<>();
        result.setName(DISTRIBUTION_CHART_NAME);
        result.setAxisx(axisx);
        result.setSeries(new ArrayList<>());
        SeriesVO<Map<String, Object>> series = new SeriesVO<>();
        series.setData(new ArrayList<>());
        for (Map<String, Object> item : list) {
            if (StringUtil.isEmpty((String) item.get("name"))) {
                item.put("name", "未知");
            }
            axisx.add((String) item.get("name"));
            series.getData().add(item);
        }
        result.getSeries().add(series);
        return result;
    }
}
