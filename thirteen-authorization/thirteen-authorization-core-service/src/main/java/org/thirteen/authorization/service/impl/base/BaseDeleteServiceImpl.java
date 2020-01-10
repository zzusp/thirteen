package org.thirteen.authorization.service.impl.base;

import org.springframework.util.Assert;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.base.BaseDeletePO;
import org.thirteen.authorization.model.vo.base.BaseDeleteVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseDeleteService;
import org.thirteen.authorization.web.PagerResult;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.thirteen.authorization.service.support.base.ModelInformation.DEL_FLAG_FIELD;
import static org.thirteen.authorization.service.support.base.ModelInformation.ID_FIELD;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口实现类（实体类包含删除标记信息）
 * @date Created in 23:08 2020/1/6
 * @modified by
 */
public abstract class BaseDeleteServiceImpl<VO extends BaseDeleteVO, PO extends BaseDeletePO>
    extends BaseServiceImpl<VO, PO> implements BaseDeleteService<VO> {

    public BaseDeleteServiceImpl(BaseRepository<PO, String> baseRepository, EntityManager em, DozerMapper dozerMapper) {
        super(baseRepository, em, dozerMapper);
    }

    @Override
    public void insert(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        model.setDelFlag(BaseDeletePO.DEL_FLAG_NORMAL);
        super.insert(model);
    }

    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        models.forEach(item -> item.setDelFlag(BaseDeletePO.DEL_FLAG_NORMAL));
        super.insertAll(models);
    }

    @Override
    public void update(VO model) {
        super.update(model);
    }

    @Override
    public void updateAll(List<VO> models) {
        super.updateAll(models);
    }

    @Override
    public void delete(String id) {
        Assert.notNull(id, "The given id must not be null!");
        // 动态sql
        String sql = String.format("UPDATE %s SET %s = %s WHERE %s = ?%d",
            this.poInformation.getTableName(), DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_DELETE, ID_FIELD, 1);
        // 创建查询对象，并执行更新语句
        this.createNativeQuery(sql, id).executeUpdate();
    }

    @Override
    public void deleteInBatch(List<String> ids) {
        Assert.notEmpty(ids, "Id collection must not be empty!");
        Iterator<String> it = ids.iterator();
        // 动态sql
        StringBuilder sql = new StringBuilder(String.format("UPDATE %s SET %s = %s WHERE",
            this.poInformation.getTableName(), DEL_FLAG_FIELD, BaseDeletePO.DEL_FLAG_DELETE));
        // 条件序号
        int i = 0;
        // 动态拼接条件
        while (it.hasNext()) {
            it.next();
            i++;
            sql.append(String.format(" %s = ?%d", ID_FIELD, i));
            if (it.hasNext()) {
                sql.append(" OR");
            }
        }
        // 创建查询对象，并执行更新语句
        this.createNativeQuery(sql.toString(), ids).executeUpdate();
    }

    @Override
    public VO findById(String id) {
        Assert.notNull(id, "The given id must not be null!");
        Optional<PO> optional = this.baseRepository.findById(id);
        return optional.filter(PO::isNotDeleted).map(this::converToVo).orElse(null);
    }

    @Override
    public PagerResult<VO> findByIds(List<String> ids) {
        Assert.notEmpty(ids, "Id collection must not be empty!");
        return PagerResult.of(ids.stream()
            .map(item -> this.baseRepository.findById(item)
                .filter(PO::isNotDeleted)
                .map(this::converToVo)
                .orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    @Override
    public PagerResult<VO> findAll() {
        return PagerResult.of(this.converToVo(this.baseRepository.findAll().stream()
            .filter(PO::isNotDeleted).collect(Collectors.toList())));
    }

    @Override
    protected String getUpdateSql(PO model, List<Object> params) {
        // 不可更新删除标记字段
        model.setDelFlag(null);
        params.add(BaseDeletePO.DEL_FLAG_NORMAL);
        // 追加等式（已被删除的数据不可更新）
        return super.getUpdateSql(model, params) + String.format(" AND %s = ?%d", DEL_FLAG_FIELD, params.size());
    }
}
