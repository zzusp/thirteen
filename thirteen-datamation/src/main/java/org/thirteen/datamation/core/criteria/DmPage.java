package org.thirteen.datamation.core.criteria;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 分页查询入参对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
public class DmPage implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 当前页码，第一页页码为0 */
    private Integer pageNum;
    /** 每页大小 */
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
