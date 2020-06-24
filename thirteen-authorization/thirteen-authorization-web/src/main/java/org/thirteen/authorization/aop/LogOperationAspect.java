package org.thirteen.authorization.aop;

import io.swagger.annotations.Api;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thirteen.authorization.common.utils.JsonUtil;
import org.thirteen.authorization.common.utils.JwtUtil;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.common.utils.WebUtil;
import org.thirteen.authorization.model.vo.SysLogOperationVO;
import org.thirteen.authorization.service.SysLogOperationService;
import org.thirteen.authorization.web.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 配置操作日志的切面，输出日志到控制台或存储到数据库
 * @date Created in 18:53 2020/3/2
 * @modified By
 */
@Aspect
@Component
public class LogOperationAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogOperationAspect.class);
    private final SysLogOperationService sysLogOperationService;
    private final HttpServletRequest request;

    @Autowired
    public LogOperationAspect(SysLogOperationService sysLogOperationService, HttpServletRequest request) {
        this.sysLogOperationService = sysLogOperationService;
        this.request = request;
    }

    /**
     * Controller层切点，这里如果需要配置多个切入点用“||”
     * 跳过登录日志controller和操作日志controller下的所有方法
     */
    @Pointcut("(execution(* org.thirteen.authorization.controller..*.insert*(..))"
        + "|| execution(* org.thirteen.authorization.controller..*.update*(..))"
        + "|| execution(* org.thirteen.authorization.controller..*.delete*(..)))"
        + "&& (!execution(* org.thirteen.authorization.controller.SysLogLoginController.*(..)))"
        + "&& (!execution(* org.thirteen.authorization.controller.SysLogOperationController.*(..)))")
    public void operationAspect() {
    }

    /**
     * 配置环绕通知,使用在方法aspect()上注册的切入点
     * Around环绕通知，围绕着方法进行执行，上述四种的功能都能实现，其必须有返回值
     *
     * @param joinPoint 切点
     */
    @Around("operationAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("=====开始执行操作环绕通知=====");
        SysLogOperationVO logOperation = new SysLogOperationVO();
        // 目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        // 方法名
        String methodName = joinPoint.getSignature().getName();
        // 参数
        Object[] arguments = joinPoint.getArgs();
        // 目标类标识
        String targetTags = "";
        // 操作类型
        String operationValue = "";
        // 操作描述
        String operationNotes = "";
        // 操作标识
        String operationTags = "";
        // 请求来源IP地址
        logOperation.setRequestPath(WebUtil.getIpAddr(this.request));
        // 当前用户编码
        logOperation.setAccount(JwtUtil.getAccount());
        // 当前应用编码
        logOperation.setApplicationCode(null);
        // 通过java反射机制获取当前执行方法的说明及描述
        try {
            // 目标类
            Class<?> targetClass = Class.forName(targetName);
            // 目标类注解
            Api api = targetClass.getAnnotation(io.swagger.annotations.Api.class);
            // 目标类标识
            targetTags = api.tags()[0];
            // 目标类中所有方法
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class<?>[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operationValue = method.getAnnotation(io.swagger.annotations.ApiOperation.class).value();
                        operationNotes = method.getAnnotation(io.swagger.annotations.ApiOperation.class).notes();
                        operationTags = method.getAnnotation(io.swagger.annotations.ApiOperation.class).tags()[0];
                        if (StringUtil.isNotEmpty(operationTags)) {
                            targetTags = operationTags;
                        }
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            // 记录本地异常日志
            logger.error(String.format("获取当前执行方法信息失败，异常方法:%s 异常代码:%s 异常信息:%s",
                targetName + "." + methodName, e.getClass().getName(), e.getMessage()), e);
        }
        // 执行被拦截方法
        ResponseResult<?> result;
        // 记录方法执行的开始时间
        logOperation.setStartTime(LocalDateTime.now());
        try {
            result = (ResponseResult<?>) joinPoint.proceed();
        } catch (Exception e) {
            result = ResponseResult.error(e.getMessage());
        }
        logOperation.setStatus(result.getStatus());
        logOperation.setMessage(result.getMessage());
        // 记录方法执行的结束时间
        logOperation.setEndTime(LocalDateTime.now());
        logOperation.setOperationValue(targetTags + "-" + operationValue);
        logOperation.setOperationNotes(operationNotes);
        logOperation.setMethod(targetName + "." + methodName + "()");
        logOperation.setArguments(JsonUtil.toJsonString(arguments));
        logOperation.setResult(JsonUtil.toJsonString(result));
        // 记录日志到数据库
        try {
            this.sysLogOperationService.insert(logOperation);
        } catch (Exception e) {
            logger.error("新增操作日志失败", e);
            logger.error(String.format("日志内容：%s", JsonUtil.toJsonString(logOperation)));
        }
        return result;
    }

}
