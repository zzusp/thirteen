package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysDictPO;
import org.thirteen.authorization.model.vo.SysDictVO;
import org.thirteen.authorization.repository.SysDictRepository;
import org.thirteen.authorization.service.SysDictService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 数据字典模块接口实现类
 * @date Created in 21:46 2020/2/21
 * @modified By
 */
@Service
public class SysDictServiceImpl extends BaseRecordServiceImpl<SysDictVO, SysDictPO, SysDictRepository>
    implements SysDictService {

    @Autowired
    public SysDictServiceImpl(SysDictRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
