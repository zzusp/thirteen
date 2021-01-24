package org.thirteen.datamation.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Column(name = "not_null", columnDefinition = "TINYINT(4) COMMENT '是否不可为NULL 0：可为NULL；1：不可为NULL'")
    private Byte notNull;
    @Column(name = "java_type", columnDefinition = "VARCHAR(50) COMMENT 'java类型 预留字段'")
    private String javaType;
    @Column(name = "db_type", columnDefinition = "VARCHAR(50) COMMENT '数据库类型'")
    private String dbType;
    @Column(name = "column_type", columnDefinition = "TINYINT(4) COMMENT '字段类型 0：主键字段；1：逻辑删除字段；2：版本号字段'")
    private Byte columnType;
    @Column(name = "length", columnDefinition = "INT COMMENT '字段长度'")
    private Integer length;
    @Column(name = "order_number", columnDefinition = "INT COMMENT '显示顺序'")
    private Integer orderNumber;
    @Column(name = "status", columnDefinition = "TINYINT(4) NOT NULL COMMENT '状态 0：禁用；1启用'")
    private Byte status;
    @Column(name = "create_by", columnDefinition = "CHAR(32) COMMENT '创建人'")
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", columnDefinition = "DATETIME COMMENT '创建时间'")
    private LocalDateTime createTime;
    @Column(name = "update_by", columnDefinition = "CHAR(32) COMMENT '更新人'")
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time", columnDefinition = "DATETIME COMMENT '更新时间'")
    private LocalDateTime updateTime;
    @Column(name = "remark", columnDefinition = "VARCHAR(255) COMMENT '备注'")
    private String remark;
    @Version
    @Column(name = "version", columnDefinition = "INT NOT NULL COMMENT '版本号'")
    private Integer version;
}
