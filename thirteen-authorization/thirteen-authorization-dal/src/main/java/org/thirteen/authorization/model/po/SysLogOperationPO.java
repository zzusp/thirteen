package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BasePO;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 操作日志实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`sys_log_operation`")
public class SysLogOperationPO extends BasePO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 操作人ID
     */
    @Column(name = "`user_id`")
    private String userId;
    /**
     * 应用ID
     */
    @Column(name = "`application_id`")
    private String applicationId;
    /**
     * 请求地址
     */
    @Column(name = "`request_path`")
    private String requestPath;
    /**
     * 操作开始时间
     */
    @Column(name = "`start_time`")
    private LocalDateTime startTime;
    /**
     * 操作结束时间
     */
    @Column(name = "`end_time`")
    private LocalDateTime endTime;
    /**
     * 操作名称
     */
    @Column(name = "`operation_value`")
    private String operationValue;
    /**
     * 操作描述
     */
    @Column(name = "`operation_notes`")
    private String operationNotes;
    /**
     * 方法
     */
    @Column(name = "`method`")
    private String method;
    /**
     * 参数
     */
    @Column(name = "`arguments`")
    private String arguments;
    /**
     * 结果
     */
    @Column(name = "`result`")
    private String result;
    /**
     * 状态码
     */
    @Column(name = "`status`")
    private Integer status;
    /**
     * 信息
     */
    @Column(name = "`message`")
    private String message;
    /**
     * 0：正常；1：删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;

}