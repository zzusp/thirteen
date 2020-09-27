package org.thirteen.datamation.core.criteria;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import org.thirteen.datamation.core.exception.ParamErrorException;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.thirteen.datamation.core.DmCodes.*;

/**
 * @author Aaron.Sun
 * @description JPA条件帮助类
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@SuppressWarnings("squid:S3252")
public class DmQuery {

    /** 每层条件个数的最大值 */
    private static final Integer MAX_CRITERIA_SIZE = 10;
    /** 条件最大深度 */
    private static final Integer MAX_DEEP = 5;

    private DmQuery() {
    }

    /**
     * 由搜索条件参数生成jpa数据查询参数对象
     *
     * @param dmSpecification datamation查询参数对象
     * @return jpa查询参数对象
     */
    public static <T> Specification<T> createSpecification(DmSpecification dmSpecification) {
        return createSpecification(dmSpecification.getCriterias());
    }

    /**
     * 由搜索条件参数生成jpa数据查询参数对象
     *
     * @param criterias 搜索条件参数集合
     * @return jpa查询参数对象
     */
    public static <T> Specification<T> createSpecification(List<DmCriteria> criterias) {
        Assert.notEmpty(criterias, "条件参数集合不可为空");
        return (Root<T> root, CriteriaQuery<?> query, javax.persistence.criteria.CriteriaBuilder cb)
            -> setCriteria(root, cb, criterias, 0);
    }

    /**
     * 由排序参数生成jpa数据查询排序对象
     *
     * @param dmSpecification datamation查询参数对象
     * @return jpa数据查询排序对象
     */
    public static Sort createSort(DmSpecification dmSpecification) {
        return createSort(dmSpecification.getSorts());
    }

    /**
     * 由排序参数生成jpa数据查询排序对象
     *
     * @param sorts 排序参数集合
     * @return jpa数据查询排序对象
     */
    public static Sort createSort(List<DmSort> sorts) {
        Assert.notEmpty(sorts, "排序参数集合不可为空");
        List<Sort.Order> orders = new ArrayList<>();
        for (DmSort item : sorts) {
            if (StringUtils.isNotEmpty(item.getField())) {
                if (StringUtils.isEmpty(item.getOrderBy()) || ASC.equals(item.getOrderBy())) {
                    orders.add(Sort.Order.asc(item.getField()));
                    continue;
                }
                if (DESC.equals(item.getOrderBy())) {
                    orders.add(Sort.Order.desc(item.getField()));
                } else {
                    throw new ParamErrorException("非法排序关键字 " + item.getOrderBy());
                }
            }
        }
        return Sort.by(orders);
    }

    /**
     * 递归创建jpa查询参数对象
     *
     * @param root 实体类root
     * @param cb jpa查询参数创建对象
     * @param criterias 搜索条件参数集合
     * @param deep 条件深度
     * @return jpa查询参数对象
     */
    @SuppressWarnings("squid:S3776")
    private static <T> Predicate setCriteria(Root<T> root, CriteriaBuilder cb, List<DmCriteria> criterias, int deep) {
        Assert.notEmpty(criterias, "条件参数集合不可为空");
        if (criterias.size() > MAX_CRITERIA_SIZE) {
            throw new ParamErrorException("条件参数集合大小不可大于10");
        }
        // 结果
        Predicate result = null;
        // 循环中的变量
        Predicate predicate;
        // 防止恶意攻击，导致栈溢出，限制深度最多为5层
        if (deep >= MAX_DEEP) {
            throw new ParamErrorException("条件深度最大为5层");
        }
        // 深度加1
        deep++;
        // 遍历条件参数集合
        for (DmCriteria item : criterias) {
            // 判断字段名是否包含"."，如果包含则continue
            if (item.getField().contains(".")) {
                continue;
            }
            // 判断条件组是否为空
            if (CollectionUtils.isNotEmpty(item.getCriterias())) {
                predicate = setCriteria(root, cb, item.getCriterias(), deep);
            } else {
                predicate = predicateHandle(root, cb, item);
            }
            // 判断jpa查询参数是否为空
            if (result == null) {
                result = predicate;
            } else if (predicate != null) {
                // 判断条件间关系（默认关系为AND）
                if (StringUtils.isEmpty(item.getRelation()) || AND.equals(item.getRelation())) {
                    result = cb.and(result, predicate);
                } else if (OR.equals(item.getRelation())) {
                    result = cb.or(result, predicate);
                } else {
                    throw new ParamErrorException("非法关系 " + item.getRelation());
                }
            }
        }
        return result;
    }

    /**
     * 谓语处理
     *
     * @param root 实体类root
     * @param cb jpa查询参数创建对象
     * @param item 搜索条件参数
     * @return 处理后的谓语对象
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Predicate predicateHandle(Root<T> root, CriteriaBuilder cb, DmCriteria item) {
        Predicate predicate = null;
        boolean hasValue = item.getValue() != null && !"".equals(String.valueOf(item.getValue()));
        boolean hasValues = CollectionUtils.isNotEmpty(item.getValues());
        // 当value不为null和空，或条件为必选时，添加该条件
        if (hasValue || hasValues || item.isRequired()) {
            // 比较操作符默认为equals
            if (StringUtils.isEmpty(item.getOperator())) {
                item.setOperator(EQUAL);
            }
            try {
                switch (item.getOperator()) {
                    case EQUAL:
                        predicate = cb.equal(root.get(item.getField()), item.getValue());
                        break;
                    case NOT_EQUAL:
                        predicate = cb.notEqual(root.get(item.getField()), item.getValue());
                        break;
                    case GT:
                        predicate = cb.gt(root.get(item.getField()), (Number) item.getValue());
                        break;
                    case GE:
                        predicate = cb.ge(root.get(item.getField()), (Number) item.getValue());
                        break;
                    case LT:
                        predicate = cb.lt(root.get(item.getField()), (Number) item.getValue());
                        break;
                    case LE:
                        predicate = cb.le(root.get(item.getField()), (Number) item.getValue());
                        break;
                    case GREATER_THAN:
                        predicate = cb.greaterThan(root.get(item.getField()), (Comparable) item.getValue());
                        break;
                    case GREATER_THAN_OR_EQUAL_TO:
                        predicate = cb.greaterThanOrEqualTo(root.get(item.getField()), (Comparable) item.getValue());
                        break;
                    case LESS_THAN:
                        predicate = cb.lessThan(root.get(item.getField()), (Comparable) item.getValue());
                        break;
                    case LESS_THAN_OR_EQUAL_TO:
                        predicate = cb.lessThanOrEqualTo(root.get(item.getField()), (Comparable) item.getValue());
                        break;
                    case LIKE:
                        predicate = cb.like(root.get(item.getField()), (String) item.getValue());
                        break;
                    case NOT_LIKE:
                        predicate = cb.notLike(root.get(item.getField()), (String) item.getValue());
                        break;
                    case IN:
                        javax.persistence.criteria.CriteriaBuilder.In<Object> in = cb.in(root.get(item.getField()));
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
