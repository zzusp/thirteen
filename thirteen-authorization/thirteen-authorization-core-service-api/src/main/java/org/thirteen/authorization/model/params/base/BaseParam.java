package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    protected List<CriteriaParam> criterias;
    @ApiParam(value = "分页参数对象")
    protected PageParam page;
    @ApiParam(value = "排序参数对象集合")
    protected List<SortParam> sorts;

    public static BaseParam of(List<CriteriaParam> criterias, PageParam page, List<SortParam> sorts) {
        return new BaseParam().criterias(criterias).page(page).sorts(sorts);
    }

    public static BaseParam of() {
        return new BaseParam().criterias(new ArrayList<>()).sorts(new ArrayList<>());
    }

    public BaseParam criterias(List<CriteriaParam> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    public BaseParam add(CriteriaParam criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    public BaseParam page(PageParam page) {
        this.page = page;
        return this;
    }

    public BaseParam sorts(List<SortParam> sorts) {
        this.sorts = new ArrayList<>();
        this.sorts.addAll(sorts);
        return this;
    }
}
