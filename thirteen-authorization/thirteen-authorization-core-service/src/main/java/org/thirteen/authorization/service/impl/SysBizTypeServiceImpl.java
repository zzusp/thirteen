package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysBizTypePO;
import org.thirteen.authorization.model.po.SysDictPO;
import org.thirteen.authorization.model.vo.SysBizTypeVO;
import org.thirteen.authorization.model.vo.SysDictVO;
import org.thirteen.authorization.repository.SysBizTypeRepository;
import org.thirteen.authorization.repository.SysDictRepository;
import org.thirteen.authorization.service.SysBizTypeService;
import org.thirteen.authorization.service.SysDictService;
import org.thirteen.authorization.service.impl.base.BaseRecordServiceImpl;

import javax.persistence.EntityManager;

/**
 * @author Aaron.Sun
 * @description 业务类型模块接口实现类
 * @date Created in 21:47 2020/2/21
 * @modified By
 */
@Service
public class SysBizTypeServiceImpl extends BaseRecordServiceImpl<SysBizTypeVO, SysBizTypePO, SysBizTypeRepository>
    implements SysBizTypeService {

    @Autowired
    public SysBizTypeServiceImpl(SysBizTypeRepository baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

}
