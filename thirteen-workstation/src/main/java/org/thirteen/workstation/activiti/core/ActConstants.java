package org.thirteen.workstation.activiti.core;

/**
 * @author Aaron.Sun
 * @description activiti工作流引擎二次封装内置常量
 * @date Created in 16:14 2022/11/22
 * @modified By
 */
public class ActConstants {

    private ActConstants() {
    }

    // 内置流程参数

    /** 流程实例ID */
    public static final String PROC_INST_ID = "procInstId";
    /** 流程发起人 */
    public static final String START_USER_ID = "startUserId";
    /** 流程状态 */
    public static final String PROC_STATUS = "procStatus";
    /** 流程发起说明，或审批说明 */
    public static final String COMMENT = "comment";

    // 内置节点参数

    /** 候选人 */
    public static final String CANDIDATE = "candidate";
    /** 审批人 */
    public static final String ASSIGNEE = "assignee";
    /** 审批状态 */
    public static final String APPROVE_STATUS = "approveStatus";
    /** 脚本代码内容 */
    public static final String SCRIPT_CONTENT = "scriptContent";


    // 流程各个状态

    /** 已撤销 */
    public static final Integer CANCEL = 0;
    /** 审批中 */
    public static final Integer PROCESSING = 1;
    /** 已通过 */
    public static final Integer APPROVED = 2;
    /** 已拒绝 */
    public static final Integer REJECT = 3;
    /** 撤销中 */
    public static final Integer CANCELING = 4;

}
