package org.thirteen.authorization.model.vo.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 通用VO（VO基类）
 * @date Created in 15:23 2018/1/11
 * @modified by
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseVO<PK> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 实体主键（唯一标识）
     */
    @ApiModelProperty(readOnly = true)
    protected PK id;

}
