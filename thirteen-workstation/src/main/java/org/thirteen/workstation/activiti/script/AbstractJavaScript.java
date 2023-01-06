package org.thirteen.workstation.activiti.script;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * @author 孙鹏
 * @description Java动态编译执行器
 * @date Created in 17:09 2022/11/25
 * @modified By
 */
public abstract class AbstractJavaScript {

    /**
     * 执行java脚本
     *
     * @param delegateExecution 委托执行对象
     */
    public abstract void execute(DelegateExecution delegateExecution);

}
