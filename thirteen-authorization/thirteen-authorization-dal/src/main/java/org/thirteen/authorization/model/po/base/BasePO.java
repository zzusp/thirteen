package org.thirteen.authorization.model.po.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 通用实体类（实体类基类）
 * @date Created in 15:23 2018/1/11
 * @modified by
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasePO<PK> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 实体主键（唯一标识）
     */
    @Id
    @Column(name = "`id`")
    protected PK id;

}
