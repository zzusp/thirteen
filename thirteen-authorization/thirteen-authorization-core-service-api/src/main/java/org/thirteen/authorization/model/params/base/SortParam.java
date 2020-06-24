package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 排序查询入参对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@ApiModel(description = "查询参数-排序参数")
@Data
@NoArgsConstructor
public class SortParam implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 升序
     */
    public static final String ASC = "asc";
    /**
     * 降序
     */
    public static final String DESC = "desc";

    @ApiParam(value = "字段名")
    private String field;
    @ApiParam(value = "排序 asc（升序）/desc（降序）")
    private String orderBy;

    public static SortParam of() {
        return new SortParam();
    }

    public static SortParam asc(String field) {
        return of().field(field).asc();
    }

    public static SortParam desc(String field) {
        return of().field(field).desc();
    }

    public SortParam field(String field) {
        this.field = field;
        return this;
    }

    public SortParam asc() {
        this.orderBy = ASC;
        return this;
    }

    public SortParam desc() {
        this.orderBy = DESC;
        return this;
    }
}
