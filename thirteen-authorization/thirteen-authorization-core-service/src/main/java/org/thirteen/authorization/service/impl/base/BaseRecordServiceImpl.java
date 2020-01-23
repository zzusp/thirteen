package org.thirteen.authorization.service.impl.base;

import org.springframework.util.Assert;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.params.base.CriteriaParam;
import org.thirteen.authorization.model.po.base.BaseRecordPO;
import org.thirteen.authorization.model.vo.base.BaseRecordVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseRecordService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.thirteen.authorization.service.support.base.ModelInformation.CODE_FIELD;
import static org.thirteen.authorization.service.support.base.ModelInformation.DEL_FLAG_FIELD;

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

    @Override
    public void insert(VO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        model.setCreateBy("");
        model.setCreateTime(LocalDateTime.now());
        super.insert(model);
    }

    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, VO_COLLECTION_MUST_NOT_BE_EMPTY);
        String createBy = "";
        LocalDateTime now = LocalDateTime.now();
        models.forEach(item -> {
            item.setCreateBy(createBy);
            item.setCreateTime(now);
        });
        super.insertAll(models);
    }

    @Override
    public void update(VO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        model.setUpdateBy("");
        model.setUpdateTime(LocalDateTime.now());
        super.update(model);
    }

    @Override
    public void updateAll(List<VO> models) {
        Assert.notEmpty(models, VO_COLLECTION_MUST_NOT_BE_EMPTY);
        String updateBy = "";
        LocalDateTime now = LocalDateTime.now();
        models.forEach(item -> {
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
}
