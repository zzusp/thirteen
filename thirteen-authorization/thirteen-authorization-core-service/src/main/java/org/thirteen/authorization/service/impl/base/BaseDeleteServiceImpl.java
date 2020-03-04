package org.thirteen.authorization.service.impl.base;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.params.base.CriteriaParam;
import org.thirteen.authorization.model.po.base.BaseDeletePO;
import org.thirteen.authorization.model.vo.base.BaseDeleteVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseDeleteService;
import org.thirteen.authorization.web.PagerResult;

import javax.persistence.EntityManager;
import java.util.List;

import static org.thirteen.authorization.service.support.base.ModelInformation.DEL_FLAG_FIELD;
import static org.thirteen.authorization.service.support.base.ModelInformation.ID_FIELD;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口实现类（实体类包含删除标记信息）
 * @date Created in 23:08 2020/1/6
 * @modified by
 */
public abstract class BaseDeleteServiceImpl<VO extends BaseDeleteVO, PO extends BaseDeletePO, R extends BaseRepository<PO, String>>
    extends BaseServiceImpl<VO, PO, R> implements BaseDeleteService<VO> {

    public BaseDeleteServiceImpl(R baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(VO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        model.setDelFlag(BaseDeletePO.DEL_FLAG_NORMAL);
        super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, VO_COLLECTION_MUST_NOT_BE_EMPTY);
        models.forEach(item -> {
            item.setDelFlag(BaseDeletePO.DEL_FLAG_NORMAL);
        });
        super.insertAll(models);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        // 动态sql
        String sql = String.format("UPDATE %s SET %s = %s WHERE %s = ?%d",
            this.poInformation.getClassName(), DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_DELETE, ID_FIELD, 1);
        // 创建查询对象，并执行更新语句
        this.createQuery(sql, id).executeUpdate();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        Assert.notEmpty(ids, ID_COLLECTION_MUST_NOT_BE_EMPTY);
        String sql = this.getDeleteInBatchSql(String.format("UPDATE %s SET %s = %s WHERE",
            this.poInformation.getClassName(), DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_DELETE), ids);
        // 创建查询对象，并执行更新语句
        this.createQuery(sql, ids).executeUpdate();
    }

    @Override
    public VO findById(String id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        return this.findOneByParam(BaseParam.of()
            .add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_NORMAL).and())
            .add(CriteriaParam.equal(ID_FIELD, id).and()));
    }

    @Override
    public PagerResult<VO> findByIds(List<String> ids) {
        Assert.notEmpty(ids, ID_COLLECTION_MUST_NOT_BE_EMPTY);
        return this.findAllByParam(BaseParam.of()
            .add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_NORMAL).and())
            .add(CriteriaParam.in(ID_FIELD, ids).and()));
    }

    @Override
    public PagerResult<VO> findAll() {
        return super.findAllByParam(BaseParam.of()
            .add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_NORMAL).and()));
    }

    @Override
    public VO findOneByParam(BaseParam param) {
        return super.findOneByParam(param.add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_NORMAL).and()));
    }

    @Override
    public PagerResult<VO> findAllByParam(BaseParam param) {
        return super.findAllByParam(param.add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_NORMAL).and()));
    }

    /**
     * 获取更新语句（不包含值为null的字段）
     *
     * @param model  PO对象
     * @param params 参数
     * @return 更新语句
     */
    @Override
    protected String getUpdateSql(PO model, List<Object> params) {
        // 不可更新删除标记字段
        model.setDelFlag(null);
        // 获取sql
        String sql = super.getUpdateSql(model, params);
        // 追加筛选条件参数
        params.add(BaseDeletePO.DEL_FLAG_NORMAL);
        // 追加筛选条件
        return sql + String.format(" AND %s = ?%d", DEL_FLAG_FIELD, params.size());
    }
}
