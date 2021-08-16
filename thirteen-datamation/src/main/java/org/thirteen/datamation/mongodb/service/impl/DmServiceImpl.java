package org.thirteen.datamation.mongodb.service.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.thirteen.datamation.mongodb.model.po.DmDataPO;
import org.thirteen.datamation.mongodb.service.DmService;

/**
 * @author Aaron.Sun
 * @description 封装的访问mongodb的接口实现类
 * @date Created in 13:54 2021/8/12
 * @modified By
 */
@Service
public class DmServiceImpl implements DmService {

    private final MongoTemplate mongoTemplate;

    public DmServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(String collectionName, DmDataPO data) {
        mongoTemplate.insert(data, collectionName);
    }
}
