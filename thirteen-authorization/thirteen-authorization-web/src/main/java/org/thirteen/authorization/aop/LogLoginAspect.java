package org.thirteen.authorization.aop;

import io.swagger.annotations.Api;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thirteen.authorization.common.utils.LogUtil;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.common.utils.WebUtil;
import org.thirteen.authorization.web.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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

    private final HttpServletRequest request;

    @Autowired
    public LogLoginAspect(HttpServletRequest request) {
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
        LogUtil.getLogger().debug("=====开始执行环绕通知=====");
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
        String requestPath = WebUtil.getIpAddr(request);
        // 当前用户ID
       //  String userId = getUserId();
        // 当前应用ID
        // String applicationId = getApplicationId();
        // 方法执行开始时间
        LocalDateTime startTime;
        // 方法执行结束时间
        LocalDateTime endTime;
        // 方法返回信息
        String message = "";
        // 通过java反射机制获取当前执行方法的说明及描述
        try {
            // 目标类
            Class targetClass = Class.forName(targetName);
            // 目标类注解
            Api api = (Api) targetClass.getAnnotation(io.swagger.annotations.Api.class);
            // 目标类标识
            targetTags = api.tags()[0];
            // 目标类中所有方法
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
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
            /*==========记录本地异常日志==========*/
            LogUtil.getLogger().error("获取当前执行方法信息失败，异常方法:{} 异常代码:{} 异常信息:{}",
                targetName + "." + methodName,
                e.getClass().getName(),
                e.getMessage());
        }
        // 执行被拦截方法
        ResponseResult result = new ResponseResult();
        // 记录方法执行的开始时间
        startTime = LocalDateTime.now();
        try {
            result = (ResponseResult) joinPoint.proceed();
            message = result.getMessage();
        } catch (Exception e) {
//            setResultMessage(result, e);
            message = result.getMessage() + e.getCause();
        }
        // 记录方法执行的结束时间
        endTime = LocalDateTime.now();
        // 记录日志到数据库
//        saveLogOperation(userId, applicationId, requestPath, startTime,
//            endTime, targetTags + "-" + operationValue, operationNotes,
//            targetName + "." + methodName + "()", JsonUtil.toString(arguments),
//            JsonUtil.toString(result), result.getStatus(), message);
        return result;
    }

}
