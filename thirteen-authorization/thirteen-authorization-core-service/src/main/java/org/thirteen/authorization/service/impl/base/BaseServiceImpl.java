package org.thirteen.authorization.service.impl.base;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.ParamErrorException;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.params.base.CriteriaParam;
import org.thirteen.authorization.model.po.base.BasePO;
import org.thirteen.authorization.model.vo.base.BaseVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseService;
import org.thirteen.authorization.service.support.base.ModelInformation;
import org.thirteen.authorization.web.PagerResult;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    private static final Integer MAX_DEEP = 5;
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
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        this.baseRepository.saveAll(this.converToPo(models));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void update(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        Assert.notNull(model.getId(), "The given id must not be null!");
        List<Object> params = new ArrayList<>();
        String sql = this.getUpdateSql(converToPo(model), params);
        // 创建查询对象，并执行更新语句
        this.createNativeQuery(sql, params).executeUpdate();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        models.forEach(this::update);
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
        Assert.notEmpty(ids, "Id collection must not be empty!");
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
    public PagerResult<VO> findByIds(List<String> ids) {
        Assert.notEmpty(ids, "Id collection must not be empty!");
        return PagerResult.of(this.converToVo(this.baseRepository.findAllById(ids)));
    }

    @Override
    public PagerResult<VO> findAll() {
        return PagerResult.of(this.converToVo(this.baseRepository.findAll()));
    }

    @Override
    public PagerResult<VO> findAllByParam(BaseParam param) {
        Assert.notNull(param, "The given param must not be null!");
        // 判断分页参数是否为空

        return null;
    }

    // ================================= 以下方法为类方法 ================================= //

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

    /**
     * 获取更新语句（不包含值为null的字段）
     *
     * @param model  PO对象
     * @param params 参数
     * @return 更新语句
     */
    protected String getUpdateSql(PO model, List<Object> params) {
        Assert.notNull(params, "params collection must not be null!");
        List<String> equations = new ArrayList<>();
        // 动态sql
        StringBuilder sql = new StringBuilder(String.format("UPDATE %s SET", this.poInformation.getTableName()));
        // 条件序号
        int i = 0;
        // while循环中，临时变量
        Object value;
        // 动态拼接条件
        for (Field field : this.poInformation.getFields()) {
            if (!ID_FIELD.equals(field.getName())) {
                value = this.poInformation.invokeGet(field.getName(), new Class[]{field.getType()}, model);
                if (Objects.nonNull(value)) {
                    i++;
                    equations.add(String.format(" %s = ?%d", field.getName(), i));
                    params.add(value);
                }
            }
        }
        sql.append(StringUtil.join(equations, ","));
        sql.append(String.format(" WHERE %s = ?%d", ID_FIELD, i + 1));
        params.add(model.getId());
        return sql.toString();
    }

    /**
     * 创建查询对象
     *
     * @param sql   sql语句
     * @param param 参数
     * @return 查询对象
     */
    protected Query createNativeQuery(String sql, Object param) {
        // 创建查询对象
        Query nativeQuery = this.em.createNativeQuery(sql, this.poInformation.getRealClass());
        nativeQuery.setParameter(1, param);
        return nativeQuery;
    }

    /**
     * 创建查询对象
     *
     * @param sql    sql语句
     * @param params 参数集合
     * @return 查询对象
     */
    protected Query createNativeQuery(String sql, List<Object> params) {
        // 创建查询对象
        Query nativeQuery = this.em.createNativeQuery(sql, this.poInformation.getRealClass());
        Iterator<Object> it = params.iterator();
        int i = 0;
        while (it.hasNext()) {
            ++i;
            nativeQuery.setParameter(i, it.next());
        }
        return nativeQuery;
    }

    /**
     * 由搜索条件参数生成jpa数据查询参数对象
     *
     * @param criteria 搜索条件参数
     * @return jpa查询参数对象
     */
    protected Specification<PO> createSpecification(CriteriaParam criteria) {
        return (Root<PO> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> this.setCriteria(root, cb, criteria, 0);
    }

    /**
     * 递归创建jpa查询参数对象
     *
     * @param root     实体类root
     * @param cb       jpa查询参数创建对象
     * @param criteria 搜索条件参数
     * @param deep     条件深度
     * @return jpa查询参数对象
     */
    private Predicate setCriteria(Root<PO> root, CriteriaBuilder cb, CriteriaParam criteria, int deep) {
        Predicate predicate = null;
        // 防止恶意攻击，导致栈溢出，限制深度最多为5层
        if (deep < MAX_DEEP) {
            // 判断条件组是否不为空
            if (criteria.getGroup() != null && criteria.getGroup().size() > 0) {
                deep++;
                for (CriteriaParam item : criteria.getGroup()) {
                    if (predicate == null) {
                        predicate = this.setCriteria(root, cb, item, deep);
                    } else {
                        if (StringUtil.isEmpty(item.getRelation()) || "AND".equals(item.getRelation())) {
                            predicate = cb.and(predicate, this.setCriteria(root, cb, item, deep));
                        } else if ("OR".equals(item.getRelation())) {
                            predicate = cb.or(predicate, this.setCriteria(root, cb, item, deep));
                        } else {
                            throw new ParamErrorException("非法关系 " + item.getRelation());
                        }
                    }
                }
            } else {
                if (StringUtil.isNotEmpty(criteria.getValue()) || criteria.isRequired()) {
                    if (StringUtil.isEmpty(criteria.getOperator())) {
                        predicate = cb.equal(root.get(criteria.getFeild()), criteria.getValue());
                    } else if ("like".equals(criteria.getOperator())) {
                        predicate = cb.like(root.get(criteria.getFeild()), criteria.getValue());
                    }
                }
            }
        } else {
            throw new ParamErrorException("条件深度最大为5层");
        }
        return predicate;
    }
}
