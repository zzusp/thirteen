package org.thirteen.datamation.service.impl;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thirteen.datamation.core.criteria.*;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.repository.BaseRepository;
import org.thirteen.datamation.core.spring.DatamationRepository;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.JsonUtil;
import org.thirteen.datamation.util.StringUtils;
import org.thirteen.datamation.web.PagerResult;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;
import static org.thirteen.datamation.core.DmCodes.DEL_FLAG_DELETE;
import static org.thirteen.datamation.core.DmCodes.DEL_FLAG_NORMAL;

/**
 * @author Aaron.Sun
 * @description 数据化通用服务接口实现
 * @date Created in 17:46 2020/9/21
 * @modified by
 */
@Service
public class DmServiceImpl implements DmService {

    private static final String SUB_DATA_KEY = "subData";

    private final DatamationRepository datamationRepository;

    public DmServiceImpl(DatamationRepository datamationRepository) {
        this.datamationRepository = datamationRepository;
    }

    @Override
    public void refresh() {
        datamationRepository.buildDatamationRepository();
    }

    @Override
    public void insert(String tableCode, Map<String, Object> model) {
        getRepository(tableCode).save(mapToPo(tableCode, model));
    }

    @Override
    public void insertAll(String tableCode, List<Map<String, Object>> models) {
        getRepository(tableCode).saveAll(mapToPo(tableCode, models));
    }

    @Override
    public void update(String tableCode, Map<String, Object> model) {
        getRepository(tableCode).save(mapToPo(tableCode, model));
    }

    @Override
    public void updateAll(String tableCode, List<Map<String, Object>> models) {
        getRepository(tableCode).saveAll(mapToPo(tableCode, models));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(String tableCode, String id) {
        // 逻辑删除标志字段
        String delFlag = getClassInfo(tableCode).getDelFlagField();
        // 判断是否包含删除标识字段
        if (delFlag != null) {
            // 查询语句
            String sql = String.format("UPDATE %s SET %s = ?1 WHERE %s = ?2",
                getClassInfo(tableCode).getClassName(),
                delFlag,
                getClassInfo(tableCode).getIdField());
            // 创建实体管理器
            EntityManager em = datamationRepository.getEntityManagerFactory().createEntityManager();
            // 开启事务
            em.getTransaction().begin();
            createQuery(em, sql, DEL_FLAG_DELETE, id).executeUpdate();
            // 提交事务
            em.getTransaction().commit();
        } else {
            getRepository(tableCode).deleteById(id);
        }
    }

    @Override
    public void deleteInBatch(String tableCode, List<String> ids) {
        // 查询参数
        Object[] params;
        // 查询语句
        String sql;
        // 待处理参数的序号
        int paramIndex;
        // 逻辑删除标志字段
        String delFlag = getClassInfo(tableCode).getDelFlagField();
        // 判断是否包含删除标识字段
        if (delFlag != null) {
            // 查询参数
            params = new Object[ids.size() + 1];
            params[0] = DEL_FLAG_DELETE;
            // 待处理参数序号为2
            paramIndex = 2;
            // 查询语句
            sql = String.format("UPDATE %s SET %s = ?1 WHERE", getClassInfo(tableCode).getClassName(), delFlag);
        } else {
            // 查询参数
            params = new Object[ids.size()];
            // 待处理参数序号为2
            paramIndex = 1;
            // 查询语句
            sql = String.format("DELETE %s WHERE",
                getClassInfo(tableCode).getClassName());
        }
        // 动态拼接查询条件，及查询参数
        List<String> equations = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            equations.add(String.format(" %s = ?%d", getClassInfo(tableCode).getIdField(), (i + paramIndex)));
            params[i + paramIndex - 1] = ids.get(i);
        }
        sql = sql + join(equations, " OR");
        // 创建实体管理器
        EntityManager em = datamationRepository.getEntityManagerFactory().createEntityManager();
        // 开启事务
        em.getTransaction().begin();
        createQuery(em, sql, params).executeUpdate();
        // 提交事务
        em.getTransaction().commit();
    }

    @Override
    public Map<String, Object> findById(String tableCode, String id) {
        return findOneBySpecification(DmSpecification.of(tableCode)
            .add(DmCriteria.equal(getClassInfo(tableCode).getIdField(), id)));
    }

    @Override
    public List<Map<String, Object>> findByIds(String tableCode, List<String> ids) {
        return findAllBySpecification(DmSpecification.of(tableCode)
            .add(DmCriteria.in(getClassInfo(tableCode).getIdField(), ids))).getList();
    }

    @Override
    public PagerResult<Map<String, Object>> findAll(String tableCode) {
        return findAllBySpecification(DmSpecification.of(tableCode));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> findOneBySpecification(DmSpecification dmSpecification) {
        // 如果包含逻辑删除字段，默认添加 未删除数据 的查询条件
        String delFlag = getClassInfo(dmSpecification.getTable()).getDelFlagField();
        if (delFlag != null) {
            dmSpecification.add(DmCriteria.equal(delFlag, DEL_FLAG_NORMAL));
        }
        Optional<?> optional = getRepository(dmSpecification.getTable()).findOne(DmQuery.createSpecification(dmSpecification));
        return optional.map(v -> lookupHandle(dmSpecification.getLookups(), poToMap(v))).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PagerResult<Map<String, Object>> findAllBySpecification(DmSpecification dmSpecification) {
        String table = dmSpecification.getTable();
        Specification<?> specification = null;
        Sort sort = null;
        // 如果包含逻辑删除字段，默认添加 未删除数据 的查询条件
        String delFlag = getClassInfo(table).getDelFlagField();
        if (delFlag != null) {
            dmSpecification.add(DmCriteria.equal(delFlag, DEL_FLAG_NORMAL));
        }
        // 判断条件参数是否为空
        if (CollectionUtils.isNotEmpty(dmSpecification.getCriterias())) {
            specification = DmQuery.createSpecification(dmSpecification);
        }
        // 判断排序参数是否为空
        if (CollectionUtils.isNotEmpty(dmSpecification.getSorts())) {
            sort = DmQuery.createSort(dmSpecification);
        }
        // 分页查询参数
        DmPage dmPage = dmSpecification.getPage();
        // 判断分页参数是否为空
        if (dmPage != null) {
            PageRequest pageRequest;
            Page<?> page;
            // 判断排序参数是否不为空，如果不为空可与分页参数组合
            if (sort != null) {
                pageRequest = PageRequest.of(dmPage.getPageNum(), dmPage.getPageSize(), sort);
            } else {
                pageRequest = PageRequest.of(dmPage.getPageNum(), dmPage.getPageSize());
            }
            // 判断条件参数是否不为空，调用对应查询方法
            if (specification != null) {
                page = getRepository(table).findAll(specification, pageRequest);
            } else {
                page = getRepository(table).findAll(pageRequest);
            }
            // 处理查询结果，直接返回
            return PagerResult.of(page.getTotalElements(), lookupHandle(dmSpecification.getLookups(), poToMap(page.getContent())));
        }

        // 判断条件参数与分页参数是否为空，调用对应的查询方法

        if (specification != null && sort != null) {
            return PagerResult.of(lookupHandle(dmSpecification.getLookups(), poToMap(getRepository(table).findAll(specification, sort))));
        }
        if (specification != null) {
            return PagerResult.of(lookupHandle(dmSpecification.getLookups(), poToMap(getRepository(table).findAll(specification))));
        }
        if (sort != null) {
            return PagerResult.of(lookupHandle(dmSpecification.getLookups(), poToMap(getRepository(table).findAll(sort))));
        }
        return PagerResult.of(lookupHandle(dmSpecification.getLookups(), poToMap(getRepository(table).findAll())));
    }

    /**
     * 关联查询处理
     *
     * @param dmLookups 关联查询对象
     * @param data      源数据查询结果
     * @return 关联查询结果
     */
    private Map<String, Object> lookupHandle(List<DmLookup> dmLookups, Map<String, Object> data) {
        return lookupHandle(dmLookups, Collections.singletonList(data)).get(0);
    }

    /**
     * 关联查询处理
     *
     * @param dmLookups 关联查询对象
     * @param dataList  源数据查询结果
     * @return 关联查询结果
     */
    private List<Map<String, Object>> lookupHandle(List<DmLookup> dmLookups, List<Map<String, Object>> dataList) {
        if (CollectionUtils.isEmpty(dmLookups) || CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        // 关联表
        String from;
        // 是否为一对一
        boolean unwind;
        // 结果集的key
        String as;
        // 外键结果集
        List<Object> localFieldValues;
        // 来源表的外键字段
        String localField;
        // 关联表的外键字段
        String foreignField;
        // 查询关联表的条件对象
        DmSpecification dmSpecification;
        // 关联表查询结果
        List<Map<String, Object>> foreignResult;
        Map<Object, List<Map<String, Object>>> foreignMap;
        for (DmLookup lookup : dmLookups) {
            from = lookup.getFrom();
            unwind = lookup.getUnwind() != null ? false : lookup.getUnwind();
            as = StringUtils.isEmpty(lookup.getAs()) ? StringUtils.lineToHump(lookup.getFrom()) : lookup.getAs();
            localFieldValues = new ArrayList<>();
            localField = lookup.getLocalField();
            foreignField = lookup.getForeignField();
            foreignResult = new ArrayList<>();
            for (Map<String, Object> data : dataList) {
                localFieldValues.add(data.get(localField));
            }
            dmSpecification = DmSpecification.of(from).add(DmCriteria.in(foreignField, localFieldValues));
            // 一次查询出关联表的所有数据，避免多次循环查询
            // 判断结果集是一对一还是一对多
            if (unwind) {
                foreignResult.add(findOneBySpecification(dmSpecification));
            } else {
                foreignResult.addAll(findAllBySpecification(dmSpecification).getList());
            }
            if (CollectionUtils.isEmpty(foreignResult)) {
                continue;
            }
            String finalForeignField = foreignField;
            // 按照外键分组
            foreignMap = foreignResult.stream().collect(Collectors.groupingBy(v -> v.get(finalForeignField), Collectors.toList()));
            // 设置查询结果到对应的结果集
            for (Map<String, Object> data : dataList) {
                data.put(as, foreignMap.get(localField));
            }
        }
        return dataList;
    }

    /**
     * 由表名获取对应的repository
     *
     * @param tableCode 表名
     * @return repository
     */
    @SuppressWarnings({"squid:S3740", "rawtypes"})
    private BaseRepository getRepository(String tableCode) {
        return (BaseRepository) datamationRepository.getRepository(tableCode);
    }

    /**
     * 由表名获取对应类信息（用于生成po）
     *
     * @param tableCode 表名
     * @return classInfo
     */
    private ClassInfo getClassInfo(String tableCode) {
        return datamationRepository.getPoClassInfo(tableCode);
    }

    /**
     * 创建查询对象
     *
     * @param em     实体管理器
     * @param sql    sql语句
     * @param params 参数集合
     * @return 查询对象
     */
    private Query createQuery(EntityManager em, String sql, Object... params) {
        // 创建查询对象
        Query query = em.createQuery(sql);
        int i = 1;
        while (i <= params.length) {
            query.setParameter(i, params[i - 1]);
            i++;
        }
        return query;
    }

    /**
     * map转po对象
     *
     * @param tableCode 表名
     * @param map       map对象
     * @return po class
     */
    private Object mapToPo(String tableCode, Map<String, Object> map) {
        Class<?> poClass = datamationRepository.getPoClass(tableCode);
        return JsonUtil.parseObject(JsonUtil.toJsonString(map), poClass);
    }

    /**
     * map转po对象
     *
     * @param tableCode 表名
     * @param maps      map对象集合
     * @return po class集合
     */
    private List<Object> mapToPo(String tableCode, List<Map<String, Object>> maps) {
        List<Object> objects = new ArrayList<>();
        Class<?> poClass = datamationRepository.getPoClass(tableCode);
        for (Map<String, Object> map : maps) {
            objects.add(JsonUtil.parseObject(JsonUtil.toJsonString(map), poClass));
        }
        return objects;
    }

    /**
     * po对象转map
     *
     * @param model po对象
     * @return map
     */
    @SuppressWarnings("unchecked")
    private <T> Map<String, Object> poToMap(T model) {
        Map<String, Object> map = new HashMap<>();
        BeanMap beanMap = BeanMap.create(model);
        for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) beanMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * po对象转map
     *
     * @param models po对象集合
     * @return map
     */
    private List<Map<String, Object>> poToMap(List<?> models) {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Object model : models) {
            maps.add(poToMap(model));
        }
        return maps;
    }
}
