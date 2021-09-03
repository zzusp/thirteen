package org.thirteen.datamation.mongodb.service.impl;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thirteen.datamation.exception.BusinessException;
import org.thirteen.datamation.mongodb.model.po.DmDataPO;
import org.thirteen.datamation.mongodb.model.query.DmQuery;
import org.thirteen.datamation.mongodb.service.DmService;
import org.thirteen.datamation.web.PagerResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Aaron.Sun
 * @description 封装的访问mongodb的接口实现类
 * @date Created in 13:54 2021/8/12
 * @modified By
 */
@Service
public class DmServiceImpl implements DmService {

    private final MongoTemplate mongoTemplate;

    public DmServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(String collectionName, DmDataPO data) {
        mongoTemplate.insert(data, collectionName);
    }

    @Override
    public void insert(String collectionName, List<DmDataPO> data) {
        mongoTemplate.insert(data, collectionName);
    }

    @Override
    public void delete(DmQuery query) {

    }

    @Override
    public void update(DmQuery query) {
        List<DmQuery.UpdateValue> values = query.getUpdateValues();
        if (ObjectUtils.isEmpty(values)) {
            throw new BusinessException("需指定要变更的数据");
        }
        String code = query.getTableCode();
        Update data = new Update();
        for (DmQuery.UpdateValue value : values) {
            data.set(value.getField(), value.getValue());
        }
        mongoTemplate.upsert(new Query(Criteria.where("_id").is(query.getId())), data, code);
    }

    @Override
    public DmDataPO get(DmQuery query) {
        return null;
    }

    @Override
    public List<DmDataPO> list(DmQuery query) {
        return Collections.emptyList();
    }

    @Override
    public PagerResult<DmDataPO> page(DmQuery query) {
        return PagerResult.empty();
    }

    /**
     * 获取mongo条件对象
     *
     * @param criteria mongo条件对象
     * @param and dm条件对象集合
     * @param or dm条件对象集合
     * @return mongo条件对象
     */
    private Criteria getCriteria(Criteria criteria, List<DmQuery.Criteria> and, List<DmQuery.Criteria> or) {
        if (!ObjectUtils.isEmpty(and) && !ObjectUtils.isEmpty(or)) {
            throw new BusinessException("and 和 or 不能在同一层同时使用");
        }
        Criteria result = null;
        List<Criteria> list = new ArrayList<>();
        if (criteria != null) {
            list.add(criteria);
        }
        // 与
        if (!ObjectUtils.isEmpty(and)) {
            Criteria temp;
            for (DmQuery.Criteria c : and) {
                temp = getCriteria(this.criteriaMapping(c), c.getAnd(), c.getOr());
                if (temp != null) {
                    list.add(temp);
                }
            }
            result = new Criteria().andOperator(list.toArray(new Criteria[0]));
        }
        // 或
        if (!ObjectUtils.isEmpty(or)) {
            Criteria temp;
            for (DmQuery.Criteria c : or) {
                temp = getCriteria(this.criteriaMapping(c), c.getAnd(), c.getOr());
                if (temp != null) {
                    list.add(temp);
                }
            }
            result = new Criteria().orOperator(list.toArray(new Criteria[0]));
        }
        return result;
    }

    /**
     * 条件对象间的映射转换
     *
     * @param dmCriteria dm的条件对象
     * @return mongo的条件对象
     */
    private Criteria criteriaMapping(DmQuery.Criteria dmCriteria) {
        if (dmCriteria == null || (!dmCriteria.isRequired() && ObjectUtils.isEmpty(dmCriteria.getValue())
                && org.springframework.util.CollectionUtils.isEmpty(dmCriteria.getValues()))) {
            return null;
        }
        Criteria criteria = Criteria.where(dmCriteria.getField());
        String operator = dmCriteria.getOperator();
        // 判断参数是否为集合类型
        boolean flag = DmQuery.Criteria.IN.equals(operator)
                || DmQuery.Criteria.NIN.equals(operator)
                || DmQuery.Criteria.MOD.equals(operator)
                || DmQuery.Criteria.GT_AND_LT.equals(operator)
                || DmQuery.Criteria.GTE_AND_LT.equals(operator)
                || DmQuery.Criteria.GTE_AND_LTE.equals(operator)
                || DmQuery.Criteria.GT_AND_LTE.equals(operator)
                || DmQuery.Criteria.GT_OR_LT.equals(operator)
                || DmQuery.Criteria.GTE_OR_LT.equals(operator)
                || DmQuery.Criteria.GTE_OR_LTE.equals(operator)
                || DmQuery.Criteria.GT_OR_LTE.equals(operator);
        if (dmCriteria.getValues() == null && flag) {
            dmCriteria.setValues(Arrays.asList(dmCriteria.getValue().toString().split(",")));
        }
        switch (dmCriteria.getOperator()) {
            case DmQuery.Criteria.IS:
                criteria = criteria.is(dmCriteria.getValue());
                break;
            case DmQuery.Criteria.NE:
                criteria = criteria.ne(dmCriteria.getValue());
                break;
            case DmQuery.Criteria.LT:
                criteria = criteria.lt(dmCriteria.getValue());
                break;
            case DmQuery.Criteria.LTE:
                criteria = criteria.lte(dmCriteria.getValue());
                break;
            case DmQuery.Criteria.GT:
                criteria = criteria.gt(dmCriteria.getValue());
                break;
            case DmQuery.Criteria.GTE:
                criteria = criteria.gte(dmCriteria.getValue());
                break;
            case DmQuery.Criteria.IN:
                criteria = criteria.in(dmCriteria.getValues());
                break;
            case DmQuery.Criteria.NIN:
                criteria = criteria.nin(dmCriteria.getValues());
                break;
            case DmQuery.Criteria.MOD:
                criteria = criteria.mod((Number) dmCriteria.getValues().get(0), (Number) dmCriteria.getValues().get(1));
                break;
            case DmQuery.Criteria.SIZE:
                criteria = criteria.size((int) dmCriteria.getValue());
                break;
            case DmQuery.Criteria.EXISTS:
                criteria = criteria.exists((boolean) dmCriteria.getValue());
                break;
            case DmQuery.Criteria.TYPE:
                criteria = criteria.type((int) dmCriteria.getValue());
                break;
            case DmQuery.Criteria.NOT:
                criteria = criteria.not();
                break;
            case DmQuery.Criteria.REGEX:
                criteria = criteria.regex(dmCriteria.getValue().toString());
                break;
            case DmQuery.Criteria.GT_AND_LT:
                criteria = criteria.gt(dmCriteria.getValues().get(0)).lt(dmCriteria.getValues().get(1));
                break;
            case DmQuery.Criteria.GTE_AND_LT:
                criteria = criteria.gte(dmCriteria.getValues().get(0)).lt(dmCriteria.getValues().get(1));
                break;
            case DmQuery.Criteria.GTE_AND_LTE:
                criteria = criteria.gte(dmCriteria.getValues().get(0)).lte(dmCriteria.getValues().get(1));
                break;
            case DmQuery.Criteria.GT_AND_LTE:
                criteria = criteria.gt(dmCriteria.getValues().get(0)).lte(dmCriteria.getValues().get(1));
                break;
            case DmQuery.Criteria.LIKE:
                String value = escapeExprSpecialWord(dmCriteria.getValue().toString());
                if (value.contains("%")) {
                    value = value + ".*";
                }
                criteria = criteria.regex("^" + value + "$");
                break;
            case DmQuery.Criteria.CONTAINS:
                criteria = criteria.regex(Pattern.compile("^.*" +
                        escapeExprSpecialWord(dmCriteria.getValue().toString()) + ".*$", Pattern.CASE_INSENSITIVE));
                break;
            default:
                break;
        }
        return criteria;
    }

    /**
     * mcp排序参数处理
     *
     * @param sorts mcp排序参数
     * @return mongodb排序参数
     */
    private Sort getSort(List<DmQuery.Sort> sorts) {
        if (ObjectUtils.isEmpty(sorts)) {
            return null;
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (DmQuery.Sort sort : sorts) {
            switch (sort.getOrderBy()) {
                case DmQuery.Sort.ASC:
                    orders.add(new Sort.Order(Sort.Direction.ASC, sort.getField()));
                    break;
                case DmQuery.Sort.DESC:
                    orders.add(new Sort.Order(Sort.Direction.DESC, sort.getField()));
                    break;
                default:
                    break;
            }
        }
        return Sort.by(orders);
    }

    /**
     * mongodb的id是一个ObjectId的对象，直接返回前端会是一个json结构，所以需要转换
     *
     * @param dataList 查询结果
     * @return 处理过_id后的查询结果
     */
    private List<DmDataPO> getObjectId(List<DmDataPO> dataList) {
        if (org.springframework.util.CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        for (DmDataPO data : dataList) {
            data.put("_id", ((ObjectId) data.get("_id")).toString());
        }
        return dataList;
    }

    /**
     * regex对输入特殊字符转义
     *
     * @param keyword 字符
     * @return 转义后字符
     */
    private String escapeExprSpecialWord(String keyword) {
        if (ObjectUtils.isEmpty(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

}
