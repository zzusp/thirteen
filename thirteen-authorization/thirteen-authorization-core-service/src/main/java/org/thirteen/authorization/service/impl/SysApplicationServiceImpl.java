package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.SysApplicationPO;
import org.thirteen.authorization.model.vo.SysApplicationVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.SysApplicationService;
import org.thirteen.authorization.service.impl.base.BaseTreeSortServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * @author Aaron.Sun
 * @description 应用模块接口实现类
 * @date Created in 10:43 2018/9/14
 * @modified by
 */
@Service
public class SysApplicationServiceImpl extends BaseTreeSortServiceImpl<SysApplicationVO, SysApplicationPO>
    implements SysApplicationService {

    @Autowired
    public SysApplicationServiceImpl(BaseRepository<SysApplicationPO, String> baseRepository, EntityManagerFactory entityManagerFactory, DozerMapper dozerMapper) {
        super(baseRepository, entityManagerFactory.createEntityManager(), dozerMapper);
    }

}
