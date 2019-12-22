package org.thirteen.authorization.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.vo.base.BaseRecordVO;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 业务类型VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "业务类型")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysBizTypeVO extends BaseRecordVO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 业务类型名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 0：禁用；1启用
     */
    @ApiModelProperty(value = "是否启用")
    private String isActive;
    /**
     * 业务类型下的数据字典
     */
    @ApiModelProperty(value = "业务类型下的数据字典", hidden = true)
    private List<SysDictVO> dicts;
    /**
     * 0：正常；1：删除
     */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除")
    private String delFlag;

}