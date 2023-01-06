package org.thirteen.workstation.activiti.service;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 流程操作相关服务接口
 * @date Created in 16:11 2022/11/18
 * @modified By
 */
public interface ProcessService {

    /**
     * 发起一个流程
     *
     * @param procDefKey 流程定义key
     * @param startUserId          流程发起人
     * @param variables            流程参数
     */
    void start(String procDefKey, String startUserId, Map<String, Object> variables);

    /**
     * 节点通过（有校验用户权限）
     *
     * @param procInstId 流程实例ID
     * @param assignee          审批人
     * @param variables         业务数据
     */
    void pass(String procInstId, String assignee, Map<String, Object> variables);

    /**
     * 节点拒绝（未校验用户权限）
     *
     * @param procInstId 流程实例ID
     * @param assignee          审批人
     * @param variables         业务数据
     */
    void reject(String procInstId, String assignee, Map<String, Object> variables);

    /**
     * 撤销一个流程
     *
     * @param procInstId 流程实例ID
     * @param startUserId       流程发起人
     * @param variables         业务数据
     */
    void cancel(String procInstId, String startUserId, Map<String, Object> variables);

}
