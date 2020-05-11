package org.thirteen.authorization.aop;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thirteen.authorization.common.utils.Md5Util;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.common.utils.WebUtil;
import org.thirteen.authorization.model.vo.SysLogLoginVO;
import org.thirteen.authorization.service.SysLogLoginService;
import org.thirteen.authorization.web.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 配置登录日志的切面，输出日志到控制台或存储到数据库
 * @date Created in 2020/2/26 17:52
 * @modified by
 */
@Aspect
@Component
public class LogLoginAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogLoginAspect.class);
    private final SysLogLoginService sysLogLoginService;
    private final DatabaseReader databaseReader;
    private final HttpServletRequest request;

    @Autowired
    public LogLoginAspect(SysLogLoginService sysLogLoginService, DatabaseReader databaseReader,
                          HttpServletRequest request) {
        this.sysLogLoginService = sysLogLoginService;
        this.databaseReader = databaseReader;
        this.request = request;
    }

    /**
     * Controller层切点，这里如果需要配置多个切入点用“||”
     */
    @Pointcut("(execution(* org.thirteen.authorization.controller.LoginController.login(..)))")
    public void loginAspect() {
    }

    /**
     * 配置环绕通知,使用在方法aspect()上注册的切入点
     * Around环绕通知，围绕着方法进行执行，上述四种的功能都能实现，其必须有返回值
     *
     * @param joinPoint 切点
     */
    @Around("loginAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("=====开始执行登录日志环绕通知=====");
        // 登录日志对象
        SysLogLoginVO logLogin = new SysLogLoginVO();
        // 请求来源IP地址
        logLogin.setRequestPath(WebUtil.getIpAddr(this.request));
        // 当前用户账号
        logLogin.setAccount((String) joinPoint.getArgs()[0]);
        // 登录时间
        logLogin.setLoginTime(LocalDateTime.now());
        // 执行被拦截方法
        ResponseResult<?> result;
        try {
            result = (ResponseResult<?>) joinPoint.proceed();
        } catch (Exception e) {
            result = ResponseResult.error(e.getMessage());
        }
        logLogin.setMessage(result.getMessage());
        logLogin.setStatus(result.getStatus());
        // 获取国家省份城市
        try {
            CityResponse cityResponse = this.databaseReader.city(InetAddress.getByName(logLogin.getRequestPath()));
            logLogin.setCountry(cityResponse.getCountry().getNames().get("zh-CN"));
            logLogin.setProvince(cityResponse.getMostSpecificSubdivision().getNames().get("zh-CN"));
            logLogin.setCity(cityResponse.getCity().getNames().get("zh-CN"));
            // 直辖市获取的城市为null，需单独处理
            if (StringUtil.isEmpty(logLogin.getCity())) {
                logLogin.setCity(logLogin.getProvince());
            }
        } catch (Exception e) {
            logger.error("获取国家省份城市信息失败", e);
            logLogin.setCountry("未知");
            logLogin.setProvince("未知");
            logLogin.setCity("未知");
        }
        // 记录日志到数据库
        try {
            this.sysLogLoginService.insert(logLogin);
        } catch (Exception e) {
            logger.error(String.format("记录登录日志失败，%s", e.getMessage()), e);
        }
        return result;
    }

}
