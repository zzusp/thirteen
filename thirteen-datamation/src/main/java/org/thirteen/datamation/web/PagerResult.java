package org.thirteen.datamation.web;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 响应结果集合封装实体类
 * @date Created in 17:47 2018/8/22
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class PagerResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 结果集
     */
    private List<T> list;

    public static <T> PagerResult<T> empty() {
        return new PagerResult<T>().total(0).list(Collections.emptyList());
    }

    public static <T> PagerResult<T> of(List<T> list) {
        return of(list.size(), list);
    }

    public static <T> PagerResult<T> of(long total, List<T> list) {
        return new PagerResult<T>().total(total).list(list);
    }

    public PagerResult<T> total(long total) {
        this.total = total;
        return this;
    }

    public PagerResult<T> list(List<T> list) {
        this.list = list;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isEmpty() {
        if (this.list == null) {
            return false;
        }
        return this.list.isEmpty();
    }
}