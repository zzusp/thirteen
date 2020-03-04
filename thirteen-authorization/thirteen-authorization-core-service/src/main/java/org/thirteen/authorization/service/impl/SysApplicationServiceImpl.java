package org.thirteen.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.po.SysApplicationPO;
import org.thirteen.authorization.model.vo.SysApplicationVO;
import org.thirteen.authorization.repository.SysApplicationRepository;
import org.thirteen.authorization.repository.SysRoleApplicationRepository;
import org.thirteen.authorization.service.SysApplicationService;
import org.thirteen.authorization.service.impl.base.BaseTreeSortServiceImpl;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 应用模块接口实现类
 * @date Created in 10:43 2018/9/14
 * @modified by
 */
@Service
public class SysApplicationServiceImpl
    extends BaseTreeSortServiceImpl<SysApplicationVO, SysApplicationPO, SysApplicationRepository>
    implements SysApplicationService {

    private final SysRoleApplicationRepository sysRoleApplicationRepository;

    @Autowired
    public SysApplicationServiceImpl(SysApplicationRepository baseRepository, DozerMapper dozerMapper,
                                     EntityManager em, SysRoleApplicationRepository sysRoleApplicationRepository) {
        super(baseRepository, dozerMapper, em);
        this.sysRoleApplicationRepository = sysRoleApplicationRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // 删除所有应用关联
        this.baseRepository.findById(id).ifPresent(item -> this.removeAllRelation(item.getCode()));
        super.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        // 删除所有应用关联
        ids.forEach(id -> {
            this.baseRepository.findById(id).ifPresent(item -> this.removeAllRelation(item.getCode()));
        });
        super.deleteInBatch(ids);
    }

    /**
     * 删除所有应用关联（方法名remove开头，与delete区分开，沿用调用方法的事务）
     *
     * @param applicationCode 应用编码
     */
    private void removeAllRelation(String applicationCode) throws BusinessException {
        try {
            this.sysRoleApplicationRepository.deleteByApplicationCode(applicationCode);
        } catch (Exception e) {
            throw new BusinessException("删除所有应用关联失败", e.getCause());
        }
    }
}
