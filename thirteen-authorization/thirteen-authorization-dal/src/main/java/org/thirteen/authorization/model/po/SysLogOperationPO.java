package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseDeletePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Entity
@Table(name = "sys_log_operation")
@org.hibernate.annotations.Table(appliesTo = "sys_log_operation", comment = "操作日志信息表")
public class SysLogOperationPO extends BaseDeletePO {

    private static final long serialVersionUID = 1L;
    /**
     * 账号
     */
    @Column(name = "account", columnDefinition = "VARCHAR(20) COMMENT '账号'")
    private String account;
    /**
     * 应用编码
     */
    @Column(name = "application_code", columnDefinition = "CHAR(20) COMMENT '应用编码'")
    private String applicationCode;
    /**
     * 请求地址
     */
    @Column(name = "request_path", columnDefinition = "VARCHAR(500) COMMENT '请求地址'")
    private String requestPath;
    /**
     * 操作开始时间
     */
    @Column(name = "start_time", columnDefinition = "DATETIME COMMENT '操作开始时间'")
    private LocalDateTime startTime;
    /**
     * 操作结束时间
     */
    @Column(name = "end_time", columnDefinition = "DATETIME COMMENT '操作结束时间'")
    private LocalDateTime endTime;
    /**
     * 操作名称
     */
    @Column(name = "operation_value", columnDefinition = "VARCHAR(50) COMMENT '操作名称'")
    private String operationValue;
    /**
     * 操作描述
     */
    @Column(name = "operation_notes", columnDefinition = "VARCHAR(200) COMMENT '操作描述'")
    private String operationNotes;
    /**
     * 方法
     */
    @Column(name = "method", columnDefinition = "VARCHAR(100) COMMENT '方法'")
    private String method;
    /**
     * 参数
     */
    @Column(name = "arguments", columnDefinition = "VARCHAR(1000) COMMENT '参数'")
    private String arguments;
    /**
     * 结果
     */
    @Column(name = "result", columnDefinition = "VARCHAR(1000) COMMENT '结果'")
    private String result;
    /**
     * 状态码
     */
    @Column(name = "status", columnDefinition = "INT COMMENT '状态码'")
    private Integer status;
    /**
     * 信息
     */
    @Column(name = "message", columnDefinition = "VARCHAR(500) COMMENT '信息'")
    private String message;
    /**
     * 不需要版本号
     */
    @Transient
    private Integer version;

}