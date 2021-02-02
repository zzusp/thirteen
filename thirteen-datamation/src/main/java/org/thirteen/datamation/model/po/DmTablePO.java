package org.thirteen.datamation.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.thirteen.datamation.model.vo.DmTableVO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Transient
    private List<DmColumnPO> columns = new ArrayList<>();

    /**
     * vo转po
     *
     * @param vo vo对象
     * @return po对象
     */
    public static DmTablePO convert(DmTableVO vo, DmTablePO po) {
        if (vo == null) {
            return null;
        }
        if (po == null) {
            po = new DmTablePO();
        }
        po.setId(vo.getId());
        po.setCode(vo.getCode());
        po.setName(vo.getName());
        po.setStatus(vo.getStatus());
        if (vo.getCreateBy() != null) {
            po.setCreateBy(vo.getCreateBy());
        }
        if (vo.getCreateTime() != null) {
            po.setCreateTime(vo.getCreateTime());
        }
        if (vo.getUpdateBy() != null) {
            po.setUpdateBy(vo.getUpdateBy());
        }
        if (vo.getUpdateTime() != null) {
            po.setUpdateTime(vo.getUpdateTime());
        }
        po.setRemark(vo.getRemark());
        po.setVersion(vo.getVersion());
        po.setColumns(vo.getColumns());
        return po;
    }
}
