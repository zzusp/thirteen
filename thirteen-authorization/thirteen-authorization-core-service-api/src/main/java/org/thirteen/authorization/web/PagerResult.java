package org.thirteen.authorization.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 响应结果集合封装实体类
 * @date Created in 17:47 2018/8/22
 * @modified by
 */
@ApiModel(description = "响应结果-分页对象")
@Data
@NoArgsConstructor
public class PagerResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(example = "10", notes = "总记录数")
    private int total;
    @ApiModelProperty(notes = "结果集")
    private List<T> list;

    public static <T> PagerResult<T> empty() {
        return new PagerResult<T>().total(0).list(Collections.emptyList());
    }

    public static <T> PagerResult<T> of(List<T> list) {
        return of(list.size(), list);
    }

    public static <T> PagerResult<T> of(int total, List<T> list) {
        return new PagerResult<T>().total(total).list(list);
    }

    public PagerResult<T> total(int total) {
        this.total = total;
        return this;
    }

    public PagerResult<T> list(List<T> list) {
        this.list = list;
        return this;
    }
}