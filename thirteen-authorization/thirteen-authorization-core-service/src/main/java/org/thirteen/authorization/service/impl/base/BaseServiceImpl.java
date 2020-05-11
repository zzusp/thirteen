package org.thirteen.authorization.service.impl.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.JsonUtil;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.ParamErrorException;
import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.params.base.CriteriaParam;
import org.thirteen.authorization.model.params.base.SortParam;
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
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static org.thirteen.authorization.service.support.base.ModelInformation.ID_FIELD;

/**
 * @author Aaron.Sun
 * @description 通用Service层抽象类，仅适用于单表操作
 * @date Created in 21:43 2018/1/10
 * @modified by
 */
public abstract class BaseServiceImpl<VO extends BaseVO, PO extends BasePO, R extends BaseRepository<PO, String>>
    implements BaseService<VO> {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
    protected static final String ID_MUST_NOT_BE_NULL = "ID不能为null";
    protected static final String ID_COLLECTION_MUST_NOT_BE_EMPTY = "ID集合不能为空";
    protected static final String VO_MUST_NOT_BE_NULL = "VO对象不能为null";
    protected static final String VO_COLLECTION_MUST_NOT_BE_EMPTY = "VO集合不能为空";
    protected static final String PARAM_MUST_NOT_BE_EMPTY = "查询参数对象不能为null";
    protected static final String CRITERIA_COLLECTION_MUST_NOT_BE_EMPTY = "条件集合不能为空";
    /**
     * 每层条件的最大值
     */
    private static final Integer MAX_CRITERIA_SIZE = 10;
    /**
     * 条件最大深度
     */
    private static final Integer MAX_DEEP = 5;
    /**
     * baseRepository
     */
    protected R baseRepository;
    /**
     * 对象转换器
     */
    protected DozerMapper dozerMapper;
    /**
     * 实体类管理器
     */
    protected EntityManager em;
    /**
     * VO对象信息
     */
    protected ModelInformation<VO> voInformation;
    /**
     * PO对象信息
     */
    protected ModelInformation<PO> poInformation;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl(R baseRepository, DozerMapper dozerMapper, EntityManager em) {
        this.baseRepository = baseRepository;
        this.dozerMapper = dozerMapper;
        this.em = em;
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.voInformation = new ModelInformation<>((Class<VO>) params[0]);
        this.poInformation = new ModelInformation<>((Class<PO>) params[1]);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(VO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        model.setId(null);
        this.baseRepository.save(this.converToPo(model));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, VO_COLLECTION_MUST_NOT_BE_EMPTY);
        models.forEach(item -> item.setId(null));
        this.baseRepository.saveAll(this.converToPo(models));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void update(VO model) {
        Assert.notNull(model, VO_MUST_NOT_BE_NULL);
        Assert.notNull(model.getId(), ID_MUST_NOT_BE_NULL);
        List<Object> params = new ArrayList<>();
        String sql = this.getUpdateSql(this.converToPo(model), params);
        // 创建查询对象，并执行更新语句
        this.createQuery(sql, params).executeUpdate();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAll(List<VO> models) {
        Assert.notEmpty(models, VO_COLLECTION_MUST_NOT_BE_EMPTY);
        models.forEach(this::update);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        this.baseRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        Assert.notEmpty(ids, ID_COLLECTION_MUST_NOT_BE_EMPTY);
        String sql = this.getDeleteInBatchSql(String.format("DELETE FROM %s WHERE", this.poInformation.getClassName()),
            ids);
        // 创建查询对象，并执行更新语句
        this.createQuery(sql, ids).executeUpdate();
    }

    @Override
    public VO findById(String id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        return this.findOneByParam(BaseParam.of().add(CriteriaParam.equal(ID_FIELD, id)));
    }

    @Override
    public PagerResult<VO> findByIds(List<String> ids) {
        Assert.notEmpty(ids, ID_COLLECTION_MUST_NOT_BE_EMPTY);
        return this.findAllByParam(BaseParam.of().add(CriteriaParam.in(ID_FIELD, ids)));
    }

    @Override
    public PagerResult<VO> findAll() {
        return this.findAllByParam(BaseParam.of());
    }

    @Override
    public VO findOneByParam(BaseParam param) {
        Assert.notNull(param, PARAM_MUST_NOT_BE_EMPTY);
        Assert.notEmpty(param.getCriterias(), CRITERIA_COLLECTION_MUST_NOT_BE_EMPTY);
        Optional<PO> optional = this.baseRepository.findOne(this.createSpecification(param.getCriterias()));
        return optional.map(this::converToVo).orElse(null);
    }

    @Override
    public PagerResult<VO> findAllByParam(BaseParam param) {
        Assert.notNull(param, PARAM_MUST_NOT_BE_EMPTY);
        logger.debug(String.format("查询参数：%s", JsonUtil.toString(param)));
        PagerResult<VO> result;
        Specification<PO> specification = null;
        Sort sort = null;
        // 判断条件参数是否为空
        if (param.getCriterias() != null && param.getCriterias().size() > 0) {
            specification = this.createSpecification(param.getCriterias());
        }
        // 判断排序参数是否为空
        if (param.getSorts() != null && param.getSorts().size() > 0) {
            sort = this.createSort(param.getSorts());
        }
        // 判断分页参数是否为空
        if (param.getPage() != null) {
            PageRequest pageRequest;
            Page<PO> page;
            if (sort != null) {
                pageRequest = PageRequest.of(param.getPage().getPageNum(), param.getPage().getPageSize(), sort);
            } else {
                pageRequest = PageRequest.of(param.getPage().getPageNum(), param.getPage().getPageSize());
            }
            if (specification != null) {
                page = this.baseRepository.findAll(specification, pageRequest);
            } else {
                page = this.baseRepository.findAll(pageRequest);
            }
            result = PagerResult.of(page.getTotalElements(), this.converToVo(page.getContent()));
        } else {
            if (specification != null && sort != null) {
                return PagerResult.of(this.converToVo(this.baseRepository.findAll(specification, sort)));
            }
            if (specification != null) {
                return PagerResult.of(this.converToVo(this.baseRepository.findAll(specification)));
            }
            if (sort != null) {
                return PagerResult.of(this.converToVo(this.baseRepository.findAll(sort)));
            }
            result = PagerResult.of(this.converToVo(this.baseRepository.findAll()));
        }
        return result;
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
     * 获取批量删除语句
     *
     * @param querySql 批量删除的查询语句
     * @param ids      主键集合
     * @return 批量删除语句
     */
    protected String getDeleteInBatchSql(String querySql, List<String> ids) {
        // 动态sql
        StringBuilder sql = new StringBuilder(querySql);
        Iterator<String> it = ids.iterator();
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
        return sql.toString();
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
        StringBuilder sql = new StringBuilder(String.format("UPDATE %s SET", this.poInformation.getClassName()));
        // 条件序号
        int i = 0;
        // while循环中，临时变量
        Object value;
        // 动态拼接条件
        for (Field field : this.poInformation.getFields()) {
            if (!ID_FIELD.equals(field.getName()) && !Modifier.isFinal(field.getModifiers())) {
                value = this.poInformation.invokeGet(field.getName(), model);
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
    protected Query createQuery(String sql, Object param) {
        // 创建查询对象
        Query nativeQuery = this.em.createQuery(sql);
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
    protected Query createQuery(String sql, List<Object> params) {
        // 创建查询对象
        Query nativeQuery = this.em.createQuery(sql);
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
        return this.createSpecification(BaseParam.of().add(criteria).getCriterias());
    }

    /**
     * 由搜索条件参数生成jpa数据查询参数对象
     *
     * @param criterias 搜索条件参数集合
     * @return jpa查询参数对象
     */
    protected Specification<PO> createSpecification(List<CriteriaParam> criterias) {
        return (Root<PO> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> this.setCriteria(root, cb, criterias, 0);
    }

    /**
     * 由排序参数生成jpa数据查询排序对象
     *
     * @param sorts 排序参数集合
     * @return jpa数据查询排序对象
     */
    protected Sort createSort(List<SortParam> sorts) {
        Assert.notEmpty(sorts, "排序参数集合不可为空");
        List<Sort.Order> orders = new ArrayList<>();
        for (SortParam item : sorts) {
            if (StringUtil.isNotEmpty(item.getField())) {
                if (StringUtil.isEmpty(item.getOrderBy()) || SortParam.ASC.equals(item.getOrderBy())) {
                    orders.add(Sort.Order.asc(item.getField()));
                    continue;
                }
                if (SortParam.DESC.equals(item.getOrderBy())) {
                    orders.add(Sort.Order.desc(item.getField()));
                    continue;
                }
                throw new ParamErrorException("非法排序关键字 " + item.getOrderBy());
            }
        }
        return Sort.by(orders);
    }

    /**
     * 递归创建jpa查询参数对象
     *
     * @param root      实体类root
     * @param cb        jpa查询参数创建对象
     * @param criterias 搜索条件参数集合
     * @param deep      条件深度
     * @return jpa查询参数对象
     */
    private Predicate setCriteria(Root<PO> root, CriteriaBuilder cb, List<CriteriaParam> criterias, int deep) {
        Assert.notEmpty(criterias, "条件参数集合不可为空");
        if (criterias.size() > MAX_CRITERIA_SIZE) {
            throw new ParamErrorException("条件参数集合大小不可大于10");
        }
        // 结果
        Predicate result = null;
        // 循环中的变量
        Predicate predicate = null;
        // 防止恶意攻击，导致栈溢出，限制深度最多为5层
        if (deep >= MAX_DEEP) {
            throw new ParamErrorException("条件深度最大为5层");
        }
        // 深度加1
        deep++;
        // 遍历条件参数集合
        for (CriteriaParam item : criterias) {
            // 判断字段名是否包含"."，如果包含则continue
            if (item.getFeild().contains(".")) {
                continue;
            }
            // 判断条件组是否为空
            if (item.getCriterias() != null && item.getCriterias().size() > 0) {
                predicate = this.setCriteria(root, cb, item.getCriterias(), deep);
            } else {
                predicate = this.predicateHandle(root, cb, item);
            }
            // 判断jpa查询参数是否为空
            if (result == null) {
                result = predicate;
                continue;
            }
            if (predicate != null) {
                // 判断条件间关系（默认关系为AND）
                if (StringUtil.isEmpty(item.getRelation()) || CriteriaParam.AND.equals(item.getRelation())) {
                    result = cb.and(result, predicate);
                    continue;
                }
                if (CriteriaParam.OR.equals(item.getRelation())) {
                    result = cb.or(result, predicate);
                    continue;
                }
                throw new ParamErrorException("非法关系 " + item.getRelation());
            }
        }
        return result;
    }

    /**
     * 谓语处理
     *
     * @param root 实体类root
     * @param cb   jpa查询参数创建对象
     * @param item 搜索条件参数
     * @return 处理后的谓语对象
     */
    @SuppressWarnings("unchecked")
    private Predicate predicateHandle(Root<PO> root, CriteriaBuilder cb, CriteriaParam item) {
        Predicate predicate = null;
        boolean hasValue = item.getValue() != null && !"".equals(String.valueOf(item.getValue()));
        boolean hasValues = item.getValues() != null && item.getValues().size() > 0;
        // 当value不为null和空，或条件为必选时，添加该条件
        if (hasValue || hasValues || item.isRequired()) {
            // 比较操作符默认为equals
            if (StringUtil.isEmpty(item.getOperator())) {
                item.setOperator(CriteriaParam.EQUAL);
            }
            try {
                switch (item.getOperator()) {
                    case CriteriaParam.EQUAL:
                        predicate = cb.equal(root.get(item.getFeild()), item.getValue());
                        break;
                    case CriteriaParam.NOT_EQUAL:
                        predicate = cb.notEqual(root.get(item.getFeild()), item.getValue());
                        break;
                    case CriteriaParam.GT:
                        predicate = cb.gt(root.get(item.getFeild()), (Number) item.getValue());
                        break;
                    case CriteriaParam.GE:
                        predicate = cb.ge(root.get(item.getFeild()), (Number) item.getValue());
                        break;
                    case CriteriaParam.LT:
                        predicate = cb.lt(root.get(item.getFeild()), (Number) item.getValue());
                        break;
                    case CriteriaParam.LE:
                        predicate = cb.le(root.get(item.getFeild()), (Number) item.getValue());
                        break;
                    case CriteriaParam.GREATER_THAN:
                        predicate = cb.greaterThan(root.get(item.getFeild()), (Comparable) item.getValue());
                        break;
                    case CriteriaParam.GREATER_THAN_OR_EQUAL_TO:
                        predicate = cb.greaterThanOrEqualTo(root.get(item.getFeild()), (Comparable) item.getValue());
                        break;
                    case CriteriaParam.LESS_THEN:
                        predicate = cb.lessThan(root.get(item.getFeild()), (Comparable) item.getValue());
                        break;
                    case CriteriaParam.LESS_THAN_OR_EQUAL_TO:
                        predicate = cb.lessThanOrEqualTo(root.get(item.getFeild()), (Comparable) item.getValue());
                        break;
                    case CriteriaParam.LIKE:
                        predicate = cb.like(root.get(item.getFeild()), (String) item.getValue());
                        break;
                    case CriteriaParam.NOT_LIKE:
                        predicate = cb.notLike(root.get(item.getFeild()), (String) item.getValue());
                        break;
                    case CriteriaParam.IN:
                        CriteriaBuilder.In<Object> in = cb.in(root.get(item.getFeild()));
                        for (Object object : item.getValues()) {
                            in.value(object);
                        }
                        predicate = in;
                        break;
                    default:
                        throw new ParamErrorException("非法比较操作符 " + item.getOperator());
                }
            } catch (ParamErrorException e) {
                throw new ParamErrorException(e.getMessage(), e.getCause());
            } catch (Exception e) {
                throw new ParamErrorException(String.format("创建条件失败，%s", e.getMessage()), e.getCause());
            }
        }
        return predicate;
    }
}
