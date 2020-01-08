package org.thirteen.authorization.service.impl.base;

import org.springframework.util.Assert;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.base.BaseDeletePO;
import org.thirteen.authorization.model.vo.base.BaseDeleteVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseDeleteService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
        this.baseRepository.save(this.converToPo(model).normal());
    }

    @Override
    public void insertAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        this.baseRepository.saveAndFlush(this.converToPo(model).normal());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        this.baseRepository.saveAll((List<PO>) this.converToPo(models).stream()
            .map(PO::normal).collect(Collectors.toList()));
    }

    @Override
    public void updateSelective(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        model.setDelFlag(null);
        super.updateSelective(model);
    }

    @Override
    public void updateSelectiveAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        model.setDelFlag(null);
        super.updateSelectiveAndFlush(model);
    }

    @Override
    public void updateSelectiveAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        models.forEach(item -> item.setDelFlag(null));
        super.updateSelectiveAll(models);
    }

    @Override
    public void delete(String id) {
        Assert.notNull(id, "The given id must not be null!");
        // 动态sql
        String sql = String.format("UPDATE %s SET %s = %s WHERE %s = ?%d",
            this.poInformation.getTableName(), DEL_FLAG_FIELD, PO.DEL_FLAG_DELETE, ID_FIELD, 1);
        // 创建查询对象
        Query nativeQuery = this.em.createNativeQuery(sql, this.poInformation.getRealClass());
        nativeQuery.setParameter(1, id);
        // 执行更新语句
        nativeQuery.executeUpdate();
    }

    @Override
    public void deleteInBatch(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        Iterator<String> it = ids.iterator();
        // 动态sql
        StringBuilder sql = new StringBuilder(String.format("UPDATE %s SET %s = %s WHERE",
            this.poInformation.getTableName(), DEL_FLAG_FIELD, PO.DEL_FLAG_DELETE));
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
        // 创建查询对象
        Query nativeQuery = this.em.createNativeQuery(sql.toString(), this.poInformation.getRealClass());
        i = 0;
        while (it.hasNext()) {
            ++i;
            nativeQuery.setParameter(i, it.next());
        }
        // 执行更新语句
        nativeQuery.executeUpdate();
    }

    @Override
    public VO findById(String id) {
        Assert.notNull(id, "The given id must not be null!");
        Optional<PO> optional = this.baseRepository.findById(id);
        return optional.filter(PO::isNotDeleted).map(this::converToVo).orElse(null);
    }

    @Override
    public List<VO> findByIds(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        return ids.stream()
            .map(item -> this.baseRepository.findById(item)
                .filter(PO::isNotDeleted)
                .map(this::converToVo)
                .orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public List<VO> findAll() {
        return this.converToVo(this.baseRepository.findAll().stream()
            .filter(PO::isNotDeleted).collect(Collectors.toList()));
    }

}
