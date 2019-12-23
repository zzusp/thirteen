package org.thirteen.authorization.model.po.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 所有包含编码，创建，更新，备注，删除标记信息的实体类的基类
 * @date Created in 17:17 2018/1/11
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseRecordPO<PK> extends BasePO<PK> {

    private static final long serialVersionUID = 1L;
    /**
     * 编码唯一，非空且不可更改
     */
    @Column(name = "`code`")
    protected String code;
    /**
     * 创建者ID/账号/编码（推荐账号）
     */
    @Column(name = "`create_by`")
    protected String createBy;
    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    protected LocalDateTime createTime;
    /**
     * 更新者ID/账号/编码（推荐账号）
     */
    @Column(name = "`update_by`")
    protected String updateBy;
    /**
     * 更新时间
     */
    @Column(name = "`update_time`")
    protected LocalDateTime updateTime;
    /**
     * 备注
     */
    @Column(name = "`remark`")
    protected String remark;

}
