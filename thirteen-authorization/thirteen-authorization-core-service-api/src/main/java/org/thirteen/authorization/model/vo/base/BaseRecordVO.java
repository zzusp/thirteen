package org.thirteen.authorization.model.vo.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 所有包含创建，更新，备注，删除标记信息的VO的基类
 * @date Created in 17:17 2018/1/11
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseRecordVO<PK> extends BaseVO<PK> {

    private static final long serialVersionUID = 1L;
    /**
     * 编码唯一，非空且不可更改
     */
    @ApiModelProperty(notes = "编码")
    protected String code;
    /**
     * 创建者ID/账号/编码（推荐账号）
     */
    @ApiModelProperty(notes = "创建者", hidden = true)
    protected String createBy;
    /**
     * 创建时间
     * <p>
     * FastJson包使用注解
     * Jackson包使用注解 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
     * 格式化前台日期参数注解 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     * <p>
     */
    @ApiModelProperty(notes = "创建时间", hidden = true, example = "1970-01-01 08:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;
    /**
     * 更新者ID/账号/编码（推荐账号）
     */
    @ApiModelProperty(notes = "更新者", hidden = true)
    protected String updateBy;
    /**
     * 更新时间
     * <p>
     * FastJson包使用注解
     * Jackson包使用注解 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
     * 格式化前台日期参数注解 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     * <p>
     */
    @ApiModelProperty(notes = "更新时间", hidden = true, example = "1970-01-01 08:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateTime;
    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注", hidden = true)
    protected String remark;

}
