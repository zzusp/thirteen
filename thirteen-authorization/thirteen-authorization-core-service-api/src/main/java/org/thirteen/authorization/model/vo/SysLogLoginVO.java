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

import java.util.Date;

/**
 * @author Aaron.Sun
 * @description 登录日志VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "登录日志")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogLoginVO extends BaseVO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String requestPath;
    /**
     * 登陆时间
     */
    @ApiModelProperty(value = "登陆时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String account;
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
     * 国家
     */
    @ApiModelProperty(value = "国家")
    private String country;
    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;
    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;
    /**
     * 0：正常；1：删除
     */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除")
    private String delFlag;

}