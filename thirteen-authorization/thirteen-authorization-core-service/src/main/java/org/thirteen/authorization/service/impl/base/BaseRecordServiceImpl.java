package org.thirteen.authorization.service.impl.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.params.base.CriteriaParam;
import org.thirteen.authorization.model.po.base.BaseRecordPO;
import org.thirteen.authorization.model.vo.base.BaseRecordVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseRecordService;
import org.thirteen.authorization.web.PagerResult;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.thirteen.authorization.constant.GlobalConstants.ACTIVE_ON;
import static org.thirteen.authorization.service.support.base.ModelInformation.*;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口实现类（实体类包含编码，状态，创建，更新，备注，删除标记信息）
 * @date Created in 16:39 2020/1/15
 * @modified by
 */
public abstract class BaseRecordServiceImpl<VO extends BaseRecordVO, PO extends BaseRecordPO, R extends BaseRepository<PO, String>>
    extends BaseDeleteServiceImpl<VO, PO, R> implements BaseRecordService<VO> {

    public BaseRecordServiceImpl(R baseRepository, DozerMapper dozerMapper, EntityManager em) {
        super(baseRepository, dozerMapper, em);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(VO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        // 验证应用编码是否存在（不包含已逻辑删除的应用编码）
        if (this.checkCode(model.getCode())) {
            throw new BusinessException(String.format("编码已存在，%s", model.getCode()));
        }
        model.setActive(ACTIVE_ON);
        model.setCreateBy("");
        model.setCreateTime(LocalDateTime.now());
        super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, VO_COLLECTION_MUST_NOT_BE_EMPTY);
        // 存储已存在的编码
        List<String> existsCodes = new ArrayList<>();
        String createBy = "";
        LocalDateTime now = LocalDateTime.now();
        models.forEach(item -> {
            // 验证应用编码是否存在（不包含已逻辑删除的应用编码）
            if (this.checkCode(item.getCode())) {
                existsCodes.add(item.getCode());
            }
            item.setActive(ACTIVE_ON);
            item.setCreateBy(createBy);
            item.setCreateTime(now);
        });
        if (existsCodes.size() > 0) {
            throw new BusinessException(String.format("编码已存在，%s", StringUtils.join(existsCodes.toArray(), ",")));
        }
        super.insertAll(models);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(VO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        // 编码不可修改
        model.setCode(null);
        model.setUpdateBy("");
        model.setUpdateTime(LocalDateTime.now());
        super.update(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAll(List<VO> models) {
        Assert.notEmpty(models, VO_COLLECTION_MUST_NOT_BE_EMPTY);
        String updateBy = "";
        LocalDateTime now = LocalDateTime.now();
        models.forEach(item -> {
            // 编码不可修改
            item.setCode(null);
            item.setUpdateBy(updateBy);
            item.setUpdateTime(now);
        });
        super.updateAll(models);
    }

    @Override
    public boolean checkCode(String code) {
        BaseParam param = BaseParam.of().add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseRecordPO.DEL_FLAG_NORMAL).and())
            .add(CriteriaParam.equal(CODE_FIELD, code).and());
        return this.baseRepository.findOne(this.createSpecification(param.getCriterias())).isPresent();
    }

    @Override
    public VO findByCode(String code) {
        BaseParam param = BaseParam.of().add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseRecordPO.DEL_FLAG_NORMAL).and())
            .add(CriteriaParam.equal(CODE_FIELD, code).and());
        return this.findOneByParam(param);
    }

    @Override
    public PagerResult<VO> findAllByCodes(List<String> codes) {
        BaseParam param = BaseParam.of().add(CriteriaParam.equal(DEL_FLAG_FIELD, BaseRecordPO.DEL_FLAG_NORMAL).and())
            .add(CriteriaParam.in(CODE_FIELD, codes).and());
        return this.findAllByParam(param);
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
        model.setVersion(model.getVersion() + 1);
        // 获取sql
        String sql = super.getUpdateSql(model, params);
        // 追加筛选条件参数
        params.add(model.getVersion() - 1);
        // 追加筛选条件
        return sql + String.format(" AND %s = ?%d", VERSION_FIELD, params.size());
    }
}
