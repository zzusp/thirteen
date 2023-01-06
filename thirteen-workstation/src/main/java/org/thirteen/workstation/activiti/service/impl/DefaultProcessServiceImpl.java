package org.thirteen.workstation.activiti.service.impl;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thirteen.workstation.activiti.core.ActConstants;
import org.thirteen.workstation.activiti.service.ProcessService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description 流程操作相关服务接口实现类基类
 * @date Created in 16:11 2022/11/18
 * @modified By
 */
@Service
public class DefaultProcessServiceImpl implements ProcessService {

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    protected DefaultProcessServiceImpl(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @Override
    public void start(String procDefKey, String startUserId, Map<String, Object> variables) {
        Authentication.setAuthenticatedUserId(startUserId);
        variables.put(ActConstants.START_USER_ID, startUserId);
        variables.put(ActConstants.PROC_STATUS, ActConstants.PROCESSING);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procDefKey, variables);
        // 流程实例ID
        String id = processInstance.getId();
        // 根据流程实例获取流程中的第一个task
        Task task = taskService.createTaskQuery().processInstanceId(id).singleResult();
    }

    @Override
    public void pass(String procInstId, String assignee, Map<String, Object> variables) {
        complete(procInstId, assignee, ActConstants.APPROVED, variables);
    }

    @Override
    public void reject(String procInstId, String assignee, Map<String, Object> variables) {
        complete(procInstId, assignee, ActConstants.REJECT, variables);
    }

    /**
     * 完成节点方法
     *
     * @param procInstId 流程实例ID
     * @param assignee   审批人
     * @param status     状态：通过 2；拒绝 3
     * @param variables  业务数据
     */
    protected void complete(String procInstId, String assignee, Integer status, Map<String, Object> variables) {
        // 获取流程中的当前任务节点
        Task task = getCurrentTask(procInstId);
        String taskId = task.getId();
        // 判断候选人是否存在
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
        if (CollectionUtils.isEmpty(identityLinkList)) {
            throw new ActivitiException("未找到候选人");
        }
        List<String> userIds = identityLinkList.stream().map(IdentityLink::getUserId).collect(Collectors.toList());
        if (!userIds.contains(assignee)) {
            throw new ActivitiException("当前用户没有审批权限");
        }
        // 添加说明添加到任务节点
        Object comment = variables.get(ActConstants.COMMENT);
        if (comment == null) {
            throw new ActivitiException("审批说明不可为空");
        }
        Authentication.setAuthenticatedUserId(assignee);
        taskService.addComment(taskId, procInstId, comment.toString());
        // 设置节点审批人
        taskService.setAssignee(task.getId(), assignee);
        variables.put(ActConstants.CANDIDATE, String.join(",", userIds));
        variables.put(ActConstants.ASSIGNEE, assignee);
        variables.put(ActConstants.APPROVE_STATUS, status);
        taskService.complete(taskId, variables, true);
    }

    /**
     * 查询流程实例当前的节点
     *
     * @param procInstId 流程实例ID
     * @return 流程实例当前的节点
     */
    protected Task getCurrentTask(String procInstId) {
        // 获取流程中的当前任务节点
        Task task = taskService.createTaskQuery().processInstanceId(procInstId).singleResult();
        if (task == null) {
            throw new ActivitiException("流程节点未找到或不存在");
        }
        return task;
    }

    @Override
    public void cancel(String procInstId, String startUserId, Map<String, Object> variables) {
        // 获取流程中的当前任务节点
        Task task = getCurrentTask(procInstId);
        String taskId = task.getId();
        // 添加说明添加到任务节点
        Object comment = variables.get(ActConstants.COMMENT);
        if (comment == null) {
            throw new ActivitiException("撤销说明不可为空");
        }
        Authentication.setAuthenticatedUserId(startUserId);
        taskService.addComment(taskId, procInstId, comment.toString());
        // 设置节点审批人
        taskService.setAssignee(task.getId(), startUserId);
        variables.put(ActConstants.ASSIGNEE, startUserId);
        variables.put(ActConstants.APPROVE_STATUS, ActConstants.CANCEL);
        runtimeService.setVariables(procInstId, variables);
        // 删除流程实例的同时设置删除原因
        runtimeService.deleteProcessInstance(procInstId, comment.toString());
    }
}
