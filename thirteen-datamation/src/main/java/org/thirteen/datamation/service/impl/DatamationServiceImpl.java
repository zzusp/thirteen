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
import org.thirteen.datamation.core.generate.repository.BaseRepository;
import org.thirteen.datamation.core.spring.DatamationRepository;
import org.thirteen.datamation.service.DatamationService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.JsonUtil;
import org.thirteen.datamation.web.PagerResult;

import java.util.*;

import static org.thirteen.datamation.core.DmCodes.ID_FIELD;

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
    public void insert(String tableCode, Map<String, Object> model) {
        getRepository(tableCode).save(mapToPo(tableCode, model));
    }

    @Override
    public void insertAll(String tableCode, List<Map<String, Object>> models) {

    }

    @Override
    public void update(String tableCode, Map<String, Object> model) {
        getRepository(tableCode).save(mapToPo(tableCode, model));
    }

    @Override
    public void updateAll(String tableCode, List<Map<String, Object>> models) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(String tableCode, String id) {
        getRepository(tableCode).deleteById(id);
    }

    @Override
    public void deleteInBatch(String tableCode, List<String> ids) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> findById(String tableCode, String id) {
        return poToMap(getRepository(tableCode).findById(id));
    }

    @Override
    public PagerResult<Map<String, Object>> findByIds(String tableCode, List<String> ids) {
        return findAllBySpecification(tableCode, DmSpecification.of().add(DmCriteria.in(ID_FIELD, ids)));
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
     * map转po对象
     *
     * @param tableCode 表名
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
     * @return po class
     */
    private Object mapToPo(String tableCode, List<Map<String, Object>> maps) {
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
