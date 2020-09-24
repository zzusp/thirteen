package org.thirteen.datamation.service.impl;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmQuery;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.repository.BaseRepository;
import org.thirteen.datamation.core.spring.DatamationRepository;
import org.thirteen.datamation.service.DatamationService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.JsonUtil;
import org.thirteen.datamation.web.PagerResult;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.join;
import static org.thirteen.datamation.core.DmCodes.DEL_FLAG_DELETE;

/**
 * @author Aaron.Sun
 * @description 数据化通用服务接口实现
 * @date Created in 17:46 2020/9/21
 * @modified by
 */
@Service
public class DatamationServiceImpl implements DatamationService {

    private final DatamationRepository datamationRepository;

    public DatamationServiceImpl(DatamationRepository datamationRepository) {
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
        // 判断是否包含删除标识字段
        if (containsDeleteFlag(tableCode)) {
            // 查询语句
            String sql = String.format("UPDATE %s SET %s = ?1 WHERE %s = ?2",
                getClassInfo(tableCode).getClassName(),
                getClassInfo(tableCode).getDeleteFlagField(),
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
        // 判断是否包含删除标识字段
        if (containsDeleteFlag(tableCode)) {
            // 查询参数
            params = new Object[ids.size() + 1];
            params[0] = DEL_FLAG_DELETE;
            // 待处理参数序号为2
            paramIndex = 2;
            // 查询语句
            sql = String.format("UPDATE %s SET %s = ?1 WHERE",
                getClassInfo(tableCode).getClassName(),
                getClassInfo(tableCode).getDeleteFlagField());
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

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> findById(String tableCode, String id) {
        return poToMap(getRepository(tableCode).findById(id));
    }

    @Override
    public PagerResult<Map<String, Object>> findByIds(String tableCode, List<String> ids) {
        return findAllBySpecification(tableCode, DmSpecification.of().add(DmCriteria.in(getIdField(tableCode), ids)));
    }

    @Override
    public PagerResult<Map<String, Object>> findAll(String tableCode) {
        return findAllBySpecification(tableCode, DmSpecification.of());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> findOneBySpecification(String tableCode, DmSpecification dmSpecification) {
        DmQuery dmQuery = new DmQuery();
        Optional<?> optional = getRepository(tableCode).findOne(dmQuery.createSpecification(dmSpecification.getCriterias()));
        return optional.map(this::poToMap).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PagerResult<Map<String, Object>> findAllBySpecification(String tableCode, DmSpecification dmSpecification) {
        DmQuery dmQuery = new DmQuery();
        Specification<?> specification = null;
        Sort sort = null;
        // 判断条件参数是否为空
        if (CollectionUtils.isNotEmpty(dmSpecification.getCriterias())) {
            specification = dmQuery.createSpecification(dmSpecification.getCriterias());
        }
        // 判断排序参数是否为空
        if (CollectionUtils.isNotEmpty(dmSpecification.getSorts())) {
            sort = dmQuery.createSort(dmSpecification.getSorts());
        }
        // 判断分页参数是否为空
        if (dmSpecification.getPage() != null) {
            PageRequest pageRequest;
            Page<?> page;
            // 判断排序参数是否不为空，如果不为空可与分页参数组合
            if (sort != null) {
                pageRequest = PageRequest.of(dmSpecification.getPage().getPageNum(), dmSpecification.getPage().getPageSize(), sort);
            } else {
                pageRequest = PageRequest.of(dmSpecification.getPage().getPageNum(), dmSpecification.getPage().getPageSize());
            }
            // 判断条件参数是否不为空，调用对应查询方法
            if (specification != null) {
                page = getRepository(tableCode).findAll(specification, pageRequest);
            } else {
                page = getRepository(tableCode).findAll(pageRequest);
            }
            // 处理查询结果，直接返回
            return PagerResult.of(page.getTotalElements(), poToMap(page.getContent()));
        }

        // 判断条件参数与分页参数是否为空，调用对应的查询方法

        if (specification != null && sort != null) {
            return PagerResult.of(poToMap(getRepository(tableCode).findAll(specification, sort)));
        }
        if (specification != null) {
            return PagerResult.of(poToMap(getRepository(tableCode).findAll(specification)));
        }
        if (sort != null) {
            return PagerResult.of(poToMap(getRepository(tableCode).findAll(sort)));
        }
        return PagerResult.of(poToMap(getRepository(tableCode).findAll()));
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
     * 由表名获取对应实体类的主键字段，驼峰命名形式（不支持联合主键）
     *
     * @param tableCode 表名
     * @return 主键字段，驼峰命名形式
     */
    private String getIdField(String tableCode) {
        return getClassInfo(tableCode).getIdField();
    }

    /**
     * 判断是否包含删除标识字段
     *
     * @return 是否包含删除标识字段
     */
    private boolean containsDeleteFlag(String tableCode) {
        return getClassInfo(tableCode).containsDeleteFlag();
    }

    /**
     * 创建查询对象
     *
     * @param em 实体管理器
     * @param sql sql语句
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
     * @param map map对象
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
     * @param maps map对象集合
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
