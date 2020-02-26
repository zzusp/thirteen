package org.thirteen.authorization.model.po.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseRecordPO extends BaseDeletePO {

    private static final long serialVersionUID = 1L;
    @Column(name = "code", columnDefinition = "CHAR(20) NOT NULL COMMENT '编码唯一，非空且不可更改'")
    protected String code;
    @Column(name = "name", columnDefinition = "VARCHAR(50) NOT NULL COMMENT '名称'")
    protected String name;
    @Column(name = "active", columnDefinition = "CHAR(1) NOT NULL COMMENT '启用标记 0：禁用；1启用'")
    protected String active;
    @Column(name = "create_by", columnDefinition = "VARCHAR(50) COMMENT '创建者ID/账号/编码（推荐账号）'")
    protected String createBy;
    @Column(name = "create_time", columnDefinition = "DATETIME COMMENT '创建时间'")
    protected LocalDateTime createTime;
    @Column(name = "update_by", columnDefinition = "VARCHAR(50) COMMENT '更新者ID/账号/编码（推荐账号）'")
    protected String updateBy;
    @Column(name = "update_time", columnDefinition = "DATETIME COMMENT '更新时间'")
    protected LocalDateTime updateTime;
    @Column(name = "remark", columnDefinition = "VARCHAR(255) COMMENT '备注'")
    protected String remark;
    @Version
    @Column(name = "version", columnDefinition = "INT NOT NULL COMMENT '版本号'")
    protected Integer version;

}
