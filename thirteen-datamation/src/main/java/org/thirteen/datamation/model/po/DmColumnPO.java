package org.thirteen.datamation.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 数据化列信息
 * @date Created in 15:17 2020/7/27
 * @modified by
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dm_column")
@org.hibernate.annotations.Table(appliesTo = "dm_column", comment = "数据化列信息")
public class DmColumnPO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "pk_uuid", strategy = "uuid")
    @GeneratedValue(generator = "pk_uuid")
    @Column(name = "id", unique = true, columnDefinition = "CHAR(32) NOT NULL COMMENT '实体主键（唯一标识）'")
    private String id;
    @Column(name = "table_code", columnDefinition = "CHAR(20) NOT NULL COMMENT '表编码，即表名'")
    private String tableCode;
    @Column(name = "code", columnDefinition = "CHAR(20) NOT NULL COMMENT '编码唯一，非空且不可更改'")
    private String code;
    @Column(name = "name", columnDefinition = "VARCHAR(50) NOT NULL COMMENT '名称'")
    private String name;
    @Column(name = "not_null", columnDefinition = "TINYINT(1) COMMENT '是否不可为NULL 0：可为NULL；1：不可为NULL'")
    private Byte notNull;
    @Column(name = "java_type", columnDefinition = "varchar(50) NOT NULL COMMENT 'java类型'")
    private String javaType;
    @Column(name = "db_type", columnDefinition = "VARCHAR(50) COMMENT '数据库类型'")
    private String dbType;
    @Column(name = "length", columnDefinition = "INT COMMENT '字段长度'")
    private Integer length;
    @Column(name = "status", columnDefinition = "TINYINT(1) NOT NULL COMMENT '状态 0：禁用；1启用'")
    private Byte status;
    @Column(name = "create_by", columnDefinition = "CHAR(32) COMMENT '创建人'")
    private String createBy;
    @Column(name = "create_time", columnDefinition = "DATETIME COMMENT '创建时间'")
    private LocalDateTime createTime;
    @Column(name = "update_by", columnDefinition = "CHAR(32) COMMENT '更新人'")
    private String updateBy;
    @Column(name = "update_time", columnDefinition = "DATETIME COMMENT '更新时间'")
    private LocalDateTime updateTime;
    @Column(name = "remark", columnDefinition = "VARCHAR(255) COMMENT '备注'")
    private String remark;
    @Version
    @Column(name = "version", columnDefinition = "INT NOT NULL COMMENT '版本号'")
    private Integer version;
    @Column(name = "del_flag", columnDefinition = "TINYINT(1) NOT NULL COMMENT '删除标志 0：正常；1：删除'")
    private Byte delFlag;
}
