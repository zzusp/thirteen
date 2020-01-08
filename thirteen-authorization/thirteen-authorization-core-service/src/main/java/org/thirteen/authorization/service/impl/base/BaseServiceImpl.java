package org.thirteen.authorization.service.impl.base;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.model.po.base.BasePO;
import org.thirteen.authorization.model.vo.base.BaseVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseService;
import org.thirteen.authorization.service.support.base.ModelInformation;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.thirteen.authorization.service.support.base.ModelInformation.ID_FIELD;

/**
 * @author Aaron.Sun
 * @description 通用Service层抽象类，仅适用于单表操作
 * @date Created in 21:43 2018/1/10
 * @modified by
 */
public abstract class BaseServiceImpl<VO extends BaseVO, PO extends BasePO> implements BaseService<VO> {

    /** baseRepository */
    protected BaseRepository<PO, String> baseRepository;
    /** 对象转换器 */
    protected DozerMapper dozerMapper;
    /** 实体类管理器 */
    protected EntityManager em;
    /** VO对象信息 */
    protected ModelInformation<VO> voInformation;
    /** PO对象信息 */
    protected ModelInformation<PO> poInformation;

    BaseServiceImpl(BaseRepository<PO, String> baseRepository, EntityManager em, DozerMapper dozerMapper) {
        this.baseRepository = baseRepository;
        this.em = em;
        this.dozerMapper = dozerMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        this.baseRepository.save(this.converToPo(model));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        this.baseRepository.saveAndFlush(this.converToPo(model));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        this.baseRepository.saveAll(this.converToPo(models));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateSelective(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        List<String> equations = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        // 动态sql
        StringBuilder sql = new StringBuilder(String.format("UPDATE %s SET", poInformation.getTableName()));
        // 条件序号
        int i = 0;
        Object id = null;
        // while循环中，临时变量
        Object value;
        // 动态拼接条件
        for (Field field : poInformation.getFields()) {
            value = this.poInformation.invokeGet(field.getName(), new Class[]{field.getType()}, converToPo(model));
            if (ID_FIELD.equals(field.getName())) {
                id = value;
            } else if (Objects.nonNull(value)) {
                i++;
                equations.add(String.format(" %s = ?%d", field.getName(), i));
                values.add(value);
            }
        }
        sql.append(StringUtil.join(equations, ","));
        sql.append(String.format(" WHERE %s = ?%d", ID_FIELD, i + 1));
        // 断言ID不可为null
        Assert.notNull(id, "The given id must not be null!");
        values.add(id);
        // 创建查询对象
        Query nativeQuery = em.createNativeQuery(sql.toString(), poInformation.getRealClass());
        Iterator<Object> it = values.iterator();
        i = 0;
        while (it.hasNext()) {
            ++i;
            nativeQuery.setParameter(i, it.next());
        }
        // 执行更新语句
        nativeQuery.executeUpdate();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSelectiveAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        this.updateSelective(model);
        this.em.flush();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSelectiveAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        models.forEach(this::updateSelective);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Assert.notNull(id, "The given id must not be null!");
        this.baseRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        this.baseRepository.deleteInBatch(ids.stream()
            .map(item -> this.baseRepository.findById(item).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    @Override
    public VO findById(String id) {
        Assert.notNull(id, "The given id must not be null!");
        Optional<PO> optional = this.baseRepository.findById(id);
        return optional.map(this::converToVo).orElse(null);
    }

    @Override
    public List<VO> findByIds(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        return ids.stream()
            .map(item -> this.baseRepository.findById(item)
                .map(this::converToVo).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public List<VO> findAll() {
        return converToVo(this.baseRepository.findAll());
    }

    /**
     * 模型转换，PO对象转换为VO对象
     *
     * @param model PO对象
     * @return VO对象
     */
    protected VO converToVo(PO model) {
        return this.dozerMapper.map(model, this.voInformation.getRealClass());
    }

    /**
     * 模型转换，PO对象集合转换为VO对象集合
     *
     * @param models PO对象集合
     * @return VO对象集合
     */
    protected List<VO> converToVo(List<PO> models) {
        if (models == null) {
            models = new ArrayList<>();
        }
        return models.stream().map(this::converToVo).collect(Collectors.toList());
    }

    /**
     * 模型转换，VO对象转换为PO对象
     *
     * @param model VO对象
     * @return PO对象
     */
    protected PO converToPo(VO model) {
        return this.dozerMapper.map(model, this.poInformation.getRealClass());
    }

    /**
     * 模型转换，VO对象集合转换为PO对象集合
     *
     * @param models VO对象集合
     * @return PO对象集合
     */
    protected List<PO> converToPo(List<VO> models) {
        if (models == null) {
            models = new ArrayList<>();
        }
        return models.stream().map(this::converToPo).collect(Collectors.toList());
    }

}
