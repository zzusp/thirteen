package org.thirteen.datamation.service.impl;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.datamation.core.criteria.*;
import org.thirteen.datamation.core.exception.DatamationException;
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

    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void insert(DmInsert dmInsert) {
        String tableCode = dmInsert.getTable();
        Map<String, Object> model = dmInsert.getModel();
        // 主键字段
        String idField = getClassInfo(tableCode).getIdField();
        // 逻辑删除标志字段
        String delFlag = getClassInfo(tableCode).getDelFlagField();
        if (delFlag != null) {
            // 设置删除标识
            dmInsert.getModel().put(delFlag, DEL_FLAG_NORMAL);
        }
        Object po = mapToPo(tableCode, model);
        getRepository(tableCode).save(po);
        // 新增成功会回填ID，设置回填的ID到map
        Map<String, Object> poMap = poToMap(po);
        model.put(idField, poMap.get(idField));
        // 处理关联数据
        if (CollectionUtils.isNotEmpty(dmInsert.getLookups())) {
            DmInsert dmSubInsert;
            for (DmLookup dmLookup : dmInsert.getLookups()) {
                dmSubInsert = DmInsert.of(dmLookup.getFrom());
                if (dmLookup.getUnwind() == null || !dmLookup.getUnwind()) {
                    dmSubInsert.setModels((List<Map<String, Object>>) model.remove(dmLookup.getAs()));
                    if (CollectionUtils.isNotEmpty(dmSubInsert.getModels())) {
                        for (Map<String, Object> sub : dmSubInsert.getModels()) {
                            sub.put(dmLookup.getForeignField(), model.get(dmLookup.getLocalField()));
                        }
                        this.insertAll(dmSubInsert);
                    }
                } else {
                    dmSubInsert.setModel((Map<String, Object>) model.remove(dmLookup.getAs()));
                    dmSubInsert.getModel().put(dmLookup.getForeignField(), model.get(dmLookup.getLocalField()));
                    this.insert(dmSubInsert);
                }
            }
        }
    }

    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void insertAll(DmInsert dmInsert) {
        List<Map<String, Object>> models = dmInsert.getModels();
        if (CollectionUtils.isEmpty(models)) {
            return;
        }
        String tableCode = dmInsert.getTable();
        // 逻辑删除标志字段
        String delFlag = getClassInfo(tableCode).getDelFlagField();
        if (delFlag != null) {
            // 设置删除标识
            for (Map<String, Object> model : models) {
                model.put(delFlag, DEL_FLAG_NORMAL);
            }
        }
        getRepository(tableCode).saveAll(mapToPo(tableCode, models));
    }

    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void update(DmUpdate dmUpdate) {
        String tableCode = dmUpdate.getTable();
        Map<String, Object> model = dmUpdate.getModel();
        // 处理级联
        if (CollectionUtils.isNotEmpty(dmUpdate.getLookups())) {
            DmInsert dmSubInsert;
            String localField;
            String foreignField;
            for (DmLookup dmLookup : dmUpdate.getLookups()) {
                localField = dmLookup.getLocalField();
                foreignField = dmLookup.getForeignField();
                // 删除旧的关联
                this.delete(model.get(localField), dmLookup);
                dmSubInsert = DmInsert.of(dmLookup.getFrom());
                if (dmLookup.getUnwind() == null || dmLookup.getUnwind()) {
                    dmSubInsert.setModel((Map<String, Object>) model.remove(dmLookup.getAs()));
                    if (dmSubInsert.getModel() != null) {
                        dmSubInsert.getModel().put(foreignField, model.get(localField));
                        this.insert(dmSubInsert);
                    }
                } else {
                    dmSubInsert.setModels((List<Map<String, Object>>) model.remove(dmLookup.getAs()));
                    if (CollectionUtils.isNotEmpty(dmSubInsert.getModels())) {
                        for (Map<String, Object> sub : dmSubInsert.getModels()) {
                            sub.put(foreignField, model.get(localField));
                        }
                        this.insertAll(dmSubInsert);
                    }
                }
            }
        }
        // 通过转换为po实现自动的类型转换，如前端传的为数值类型，后端入库需要byte类型
        Set<String> keys = new HashSet<>(model.keySet());
        model = poToMap(mapToPo(tableCode, model));
        model.keySet().removeIf(s -> !keys.contains(s));
        // 主键字段
        String idField = getClassInfo(tableCode).getIdField();
        if (idField == null) {
            throw new DatamationException("使用update方法时，必须设置主键列");
        }
        // 逻辑删除标志字段
        String delFlag = getClassInfo(tableCode).getDelFlagField();
        if (delFlag != null) {
            model.remove(delFlag);
        }
        // 版本号字段
        String versionField = getClassInfo(tableCode).getVersionField();
        if (versionField != null && model.get(versionField) == null) {
            throw new DatamationException("请指定更新数据的版本");
        }
        // 查询参数
        List<Object> params = new ArrayList<>();
        // 动态sql
        StringBuilder sql = new StringBuilder(String.format("UPDATE %s SET", getClassInfo(tableCode).getClassName()));
        // 条件序号
        int i = 1;
        List<String> equations = new ArrayList<>();
        // 动态拼接条件
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!idField.equals(entry.getKey())) {
                equations.add(String.format(" %s = ?%d", entry.getKey(), i));
                if (versionField != null && versionField.equals(entry.getKey())) {
                    params.add(((Integer) entry.getValue()) + 1);
                } else {
                    params.add(entry.getValue());
                }
                i++;
            }
        }
        sql.append(StringUtils.join(equations, ","));
        sql.append(String.format(" WHERE %s = ?%d", idField, i));
        params.add(model.get(idField));
        // 如果逻辑删除字段
        if (delFlag != null) {
            sql.append(String.format(" AND %s = ?%d", delFlag, i + 1));
            params.add(DEL_FLAG_NORMAL);
        }
        // 如果版本号字段
        if (versionField != null) {
            sql.append(String.format(" AND %s = ?%d", versionField, i + 2));
            params.add(model.get(versionField));
        }
        // 创建实体管理器
        EntityManager em = datamationRepository.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        createQuery(em, sql.toString(), params.toArray()).executeUpdate();
        em.getTransaction().commit();
    }

    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void updateAll(DmUpdate dmUpdate) {
        String tableCode = dmUpdate.getTable();
        getRepository(tableCode).saveAll(mapToPo(tableCode, dmUpdate.getModels()));
    }

    @SuppressWarnings("unchecked")
    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void delete(DmDelete dmDelete) {
        String tableCode = dmDelete.getTable();
        Object id = dmDelete.getId();
        // 判断是否需要级联删除
        if (CollectionUtils.isNotEmpty(dmDelete.getLookups())) {
            String idField = getClassInfo(tableCode).getIdField();
            boolean searchFlag = false;
            for (DmLookup dmLookup : dmDelete.getLookups()) {
                if (!idField.equals(dmLookup.getLocalField())) {
                    searchFlag = true;
                    break;
                }
            }
            if (searchFlag) {
                Map<String, Object> model = this.findById(tableCode, id);
                if (model != null) {
                    for (DmLookup dmLookup : dmDelete.getLookups()) {
                        // 删除旧的关联
                        this.delete(model.get(dmLookup.getLocalField()), dmLookup);
                    }
                }
            } else {
                for (DmLookup dmLookup : dmDelete.getLookups()) {
                    // 删除旧的关联
                    this.delete(id, dmLookup);
                }
            }
        }
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
            em.getTransaction().begin();
            createQuery(em, sql, DEL_FLAG_DELETE, id).executeUpdate();
            em.getTransaction().commit();
        } else {
            getRepository(tableCode).deleteById(id);
        }
    }

    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(DmDelete dmDelete) {
        String tableCode = dmDelete.getTable();
        List<Object> ids = dmDelete.getIds();
        // 判断是否需要级联删除
        if (CollectionUtils.isNotEmpty(dmDelete.getLookups())) {
            String idField = getClassInfo(tableCode).getIdField();
            boolean searchFlag = true;
            for (DmLookup dmLookup : dmDelete.getLookups()) {
                if (!idField.equals(dmLookup.getLocalField())) {
                    searchFlag = false;
                    break;
                }
            }
            if (searchFlag) {
                List<Map<String, Object>> models = this.findByIds(tableCode, ids);
                if (CollectionUtils.isNotEmpty(models)) {
                    for (Map<String, Object> model : models) {
                        for (DmLookup dmLookup : dmDelete.getLookups()) {
                            // 删除旧的关联
                            this.delete(model.get(dmLookup.getLocalField()), dmLookup);
                        }
                    }
                }
            } else {
                for (Object id : ids) {
                    for (DmLookup dmLookup : dmDelete.getLookups()) {
                        // 删除旧的关联
                        this.delete(id, dmLookup);
                    }
                }
            }
        }
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

    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void deleteAll(DmDelete dmDelete) {
        String tableCode = dmDelete.getTable();
        // 判断是否需要级联删除
        if (CollectionUtils.isNotEmpty(dmDelete.getLookups())) {
            PagerResult<Map<String, Object>> page = this.findAll(tableCode);
            if (CollectionUtils.isNotEmpty(page.getList())) {
                for (Map<String, Object> model : page.getList()) {
                    for (DmLookup dmLookup : dmDelete.getLookups()) {
                        // 删除旧的关联
                        this.delete(model.get(dmLookup.getLocalField()), dmLookup);
                    }
                }
            }
        }
        // 逻辑删除标志字段
        String delFlag = getClassInfo(tableCode).getDelFlagField();
        // 判断是否包含删除标识字段
        if (delFlag != null) {
            // 查询语句
            String sql = String.format("UPDATE %s SET %s = ?1", getClassInfo(tableCode).getClassName(), delFlag);
            // 创建实体管理器
            EntityManager em = datamationRepository.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            createQuery(em, sql, DEL_FLAG_DELETE).executeUpdate();
            em.getTransaction().commit();
        } else {
            getRepository(tableCode).deleteAll();
        }
    }

    @Transactional(value = "datamation:transactionManager", rollbackFor = Exception.class)
    @Override
    public void delete(Object value, DmLookup dmLookup) {
        String tableCode = dmLookup.getFrom();
        // 逻辑删除标志字段
        String delFlag = getClassInfo(tableCode).getDelFlagField();
        // 创建实体管理器
        EntityManager em = datamationRepository.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        // 判断是否包含删除标识字段
        if (delFlag != null) {
            // 查询语句
            String sql = String.format("UPDATE %s SET %s = ?1 WHERE %s = ?2",
                getClassInfo(tableCode).getClassName(), delFlag, dmLookup.getForeignField());
            createQuery(em, sql, DEL_FLAG_DELETE, value).executeUpdate();
        } else {
            String sql = String.format("DELETE %s WHERE %s = ?1",
                getClassInfo(tableCode).getClassName(), dmLookup.getForeignField());
            createQuery(em, sql, value).executeUpdate();
        }
        em.getTransaction().commit();
    }

    @Override
    public Map<String, Object> findById(String tableCode, Object id) {
        return findOneBySpecification(DmSpecification.of(tableCode)
            .add(DmCriteria.equal(getClassInfo(tableCode).getIdField(), id)));
    }

    @Override
    public List<Map<String, Object>> findByIds(String tableCode, List<Object> ids) {
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

    @Override
    public boolean isExist(DmSpecification dmSpecification) {
        PagerResult<Map<String, Object>> pagerResult = findAllBySpecification(dmSpecification);
        return !pagerResult.isEmpty();
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
            unwind = lookup.getUnwind() == null ? true : lookup.getUnwind();
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
                if (unwind) {
                    data.put(as, foreignMap.get(data.get(localField)).get(0));
                } else {
                    data.put(as, foreignMap.get(data.get(localField)));
                }
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
    @Override
    public ClassInfo getClassInfo(String tableCode) {
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
        if (CollectionUtils.isEmpty(maps)) {
            return new ArrayList<>();
        }
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
        if (CollectionUtils.isEmpty(models)) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Object model : models) {
            maps.add(poToMap(model));
        }
        return maps;
    }
}
