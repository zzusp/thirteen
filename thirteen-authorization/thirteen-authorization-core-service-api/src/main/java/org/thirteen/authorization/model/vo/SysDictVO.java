package org.thirteen.authorization.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.vo.base.BaseRecordVO;

/**
 * @author Aaron.Sun
 * @description 数据字典VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "数据字典")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDictVO extends BaseRecordVO {

    private static final long serialVersionUID = 1L;
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    private SysBizTypeVO bizType;

}