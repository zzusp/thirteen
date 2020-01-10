package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Aaron.Sun
 * @description 查询入参对象基类
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@ApiModel(description = "查询参数-基类")
@Data
@NoArgsConstructor
public class BaseParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiParam(value = "条件参数对象")
    protected CriteriaParam criteria;
    @ApiParam(value = "条件参数对象集合")
    protected List<CriteriaParam> criterias;
    @ApiParam(value = "分页参数对象")
    protected PageParam page;
    @ApiParam(value = "排序参数对象")
    protected SortParam sort;
    @ApiParam(value = "排序参数对象集合")
    protected List<SortParam> sorts;

    /**
     * 判断分页查询对象是否有效
     *
     * @return 分页查询对象是否有效
     */
    public boolean isPageabled() {
        return Objects.nonNull(this.page) && this.page.getPageNum() > 0 && this.page.getPageSize() > 0;
    }

    /**
     * 参数合法校验，如果不合法，则抛出异常
     */
    public void validate() {
    }
}
