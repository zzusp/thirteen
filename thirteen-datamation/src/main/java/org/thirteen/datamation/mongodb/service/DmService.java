package org.thirteen.datamation.mongodb.service;

import org.thirteen.datamation.mongodb.model.po.DmDataPO;

/**
 * @author Aaron.Sun
 * @description 封装的访问mongodb的接口
 * @date Created in 13:54 2021/8/12
 * @modified By
 */
public interface DmService {

    /**
     * 新增
     *
     * @param collectionName 集合名称
     * @param data 数据
     */
    void insert(String collectionName, DmDataPO data);

}
