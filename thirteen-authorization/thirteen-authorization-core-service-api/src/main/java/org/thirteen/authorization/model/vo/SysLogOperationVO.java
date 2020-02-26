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
import org.thirteen.authorization.model.vo.base.BaseDeleteVO;

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
public class SysLogOperationVO extends BaseDeleteVO {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "应用编码")
    private String applicationCode;
    @ApiModelProperty(value = "请求地址")
    private String requestPath;
    @ApiModelProperty(value = "操作开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "操作结束时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "操作名称")
    private String operationValue;
    @ApiModelProperty(value = "操作描述")
    private String operationNotes;
    @ApiModelProperty(value = "方法")
    private String method;
    @ApiModelProperty(value = "参数")
    private String arguments;
    @ApiModelProperty(value = "结果")
    private String result;
    @ApiModelProperty(value = "状态码")
    private Integer status;
    @ApiModelProperty(value = "信息")
    private String message;

}