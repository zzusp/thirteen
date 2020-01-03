package org.thirteen.authorization.model.po.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * @author Aaron.Sun
 * @description 通用实体类（包含删除标记信息的实体类的基类）
 * @date Created in 15:23 2018/1/11
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseDeletePO extends BasePO {

    private static final long serialVersionUID = 1L;
    /** 0：正常；1：删除 */
    @Column(name = "del_flag", columnDefinition = "CHAR(1) NOT NULL COMMENT '删除标记 0：正常；1：删除'")
    private String delFlag;

}
