package org.thirteen.authorization.model.po.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 通用实体类（实体类基类）
 * @date Created in 15:23 2018/1/11
 * @modified by
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BasePO implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 实体主键（唯一标识） */
    @Id
    @Column(name = "id", unique = true, columnDefinition = "CHAR(32) NOT NULL COMMENT '实体主键（唯一标识）'")
    protected String id;

}
