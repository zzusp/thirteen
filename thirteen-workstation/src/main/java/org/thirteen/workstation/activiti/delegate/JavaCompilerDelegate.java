package org.thirteen.workstation.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thirteen.workstation.activiti.core.ActConstants;
import org.thirteen.workstation.activiti.core.JavaStringCompiler;
import org.thirteen.workstation.activiti.script.AbstractJavaScript;

import java.util.Map;

/**
 * @author 孙鹏
 * @description Java动态编译执行器
 * @date Created in 17:09 2022/11/25
 * @modified By
 */
@Service("javaCompilerDelegate")
public class JavaCompilerDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(JavaCompilerDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        // 节点id
        String taskId = delegateExecution.getId();
        // 节点编码
        String taskCode = delegateExecution.getCurrentActivityId();
        // 流程参数
        Map<String, Object> params = delegateExecution.getVariables();
        Object script = params.get(ActConstants.SCRIPT_CONTENT);
        if (script == null || StringUtils.isBlank(script.toString())) {
            log.error("执行Java脚本失败，代码脚本为空");
            return;
        }
        JavaStringCompiler compiler = new JavaStringCompiler(script.toString());
        if (!compiler.compile()) {
            log.error("执行Java脚本失败，编译异常：{}", compiler.getCompilerMessage());
            return;
        }
        AbstractJavaScript instance = (AbstractJavaScript) compiler.getClassInstance();
        instance.execute(delegateExecution);
    }

}
