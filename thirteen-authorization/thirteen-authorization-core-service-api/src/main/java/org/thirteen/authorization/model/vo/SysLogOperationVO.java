package org.thirteen.authorization.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.thirteen.authorization.model.vo.base.BaseVO;

import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 操作日志VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "操作日志")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogOperationVO extends BaseVO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    private String userId;
    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    private String applicationId;
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String requestPath;
    /**
     * 操作开始时间
     */
    @ApiModelProperty(value = "操作开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    /**
     * 操作结束时间
     */
    @ApiModelProperty(value = "操作结束时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /**
     * 操作名称
     */
    @ApiModelProperty(value = "操作名称")
    private String operationValue;
    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述")
    private String operationNotes;
    /**
     * 方法
     */
    @ApiModelProperty(value = "方法")
    private String method;
    /**
     * 参数
     */
    @ApiModelProperty(value = "参数")
    private String arguments;
    /**
     * 结果
     */
    @ApiModelProperty(value = "结果")
    private String result;
    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer status;
    /**
     * 信息
     */
    @ApiModelProperty(value = "信息")
    private String message;
    /**
     * 0：正常；1：删除
     */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除")
    private String delFlag;

}