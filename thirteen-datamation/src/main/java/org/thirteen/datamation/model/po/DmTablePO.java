package org.thirteen.datamation.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Aaron.Sun
 * @description 数据化表信息
 * @date Created in 15:17 2020/7/27
 * @modified by
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dm_table")
@org.hibernate.annotations.Table(appliesTo = "dm_table", comment = "数据化表信息")
public class DmTablePO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "pk_uuid", strategy = "uuid")
    @GeneratedValue(generator = "pk_uuid")
    @Column(name = "id", unique = true, columnDefinition = "CHAR(32) NOT NULL COMMENT '实体主键（唯一标识）'")
    private String id;
    @Column(name = "code", columnDefinition = "CHAR(20) NOT NULL COMMENT '编码唯一，非空且不可更改'")
    private String code;
    @Column(name = "name", columnDefinition = "VARCHAR(50) NOT NULL COMMENT '名称'")
    private String name;
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
    @Transient
    private Set<DmColumnPO> columns = new HashSet<>();
}
