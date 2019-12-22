package org.thirteen.authorization.service.impl.base;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.authorization.common.utils.IDGenerator;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.DataNotFoundException;
import org.thirteen.authorization.exceptions.LogicDeleteErrorException;
import org.thirteen.authorization.model.po.base.BasePO;
import org.thirteen.authorization.model.vo.base.BaseVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description 通用Service层抽象类，仅适用于单表操作
 * @date Created in 21:43 2018/1/10
 * @modified by
 */
@Service
public abstract class AbstractService<VO extends BaseVO, PK, PO extends BasePO> implements BaseService<VO, PK> {

    /**
     * 逻辑删除字段
     */
    public static final String DEL_FLAG_FIELD = "delFlag";
    /**
     * 删除标记（0：正常；1：删除）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    /**
     * 当前泛型对象
     */
    private PO model;
    /**
     * baseRepository注入
     */
    protected BaseRepository<PO, PK> baseRepository;
    /**
     * 对象转换器
     */
    protected DozerMapper dozerMapper;
    /**
     * 当前泛型真实类型的Class
     */
    protected Class<VO> voClass;
    /**
     * 当前泛型真实类型的Class
     */
    protected Class<PK> pkClass;
    /**
     * 当前泛型真实类型的Class
     */
    protected Class<PO> poClass;

    @Autowired
    public AbstractService(BaseRepository<PO, PK> baseRepository, DozerMapper dozerMapper) {
        this.baseRepository = baseRepository;
        this.dozerMapper = dozerMapper;
    }

    /**
     * 通过反射获取子类确定的泛型类
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public AbstractService() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        voClass = (Class<VO>) params[0];
        pkClass = (Class<PK>) params[1];
        poClass = (Class<PO>) params[2];
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public void save(VO model) {
        // 判断主键类型是否为String类型（即uuid），如果是String类型，则需要手动设置id
        if (pkClass.equals(String.class)) {
            // 设置ID
            model.setId(IDGenerator.id());
        }
        baseRepository.save(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public void saveAndFlush(VO model) {
        // 判断主键类型是否为String类型（即uuid），如果是String类型，则需要手动设置id
        if (pkClass.equals(String.class)) {
            // 设置ID
            model.setId(IDGenerator.id());
        }
        baseRepository.saveAndFlush(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public void saveAll(List<VO> models) {
        // 判断主键类型是否为String类型（即uuid），如果是String类型，则需要手动设置id
        if (pkClass.equals(String.class)) {
            baseRepository.saveAll(models.stream().map(item -> {
                // 设置ID
                item.setId(IDGenerator.id());
                return dozerMapper.map(item, poClass);
            }).collect(Collectors.toList()));
        } else {
            baseRepository.saveAll(dozerMapper.mapList(models, poClass));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(VO model) {
        baseRepository.save(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = {Exception.class, DataNotFoundException.class})
    @SuppressWarnings("unchecked")
    @Override
    public void updateSelective(VO model) throws DataNotFoundException {
        // 更新前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById((PK) model.getId());
        // 判断数据是否存在
        if (optional.isPresent()) {
            // 如果存在则将入参中的非null属性赋给查询结果
            dozerMapper.copy(dozerMapper.map(model, poClass), optional.get(), false, true);
            baseRepository.save(optional.get());
        } else {
            throw new DataNotFoundException("更新失败，未找到主键：" + model.getId() + " 对应数据");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAndFlush(VO model) {
        baseRepository.saveAndFlush(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAll(List<VO> models) {
        baseRepository.saveAll(dozerMapper.mapList(models, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(PK id) {
        baseRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public void deleteAll(List<PK> ids) {
        baseRepository.deleteAll(ids.stream().map(item -> (PO) BasePO.builder().id(item).build())
            .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public void deleteInBatch(List<PK> ids) {
        baseRepository.deleteInBatch(ids.stream().map(item -> (PO) BasePO.builder().id(item).build())
            .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = {Exception.class, LogicDeleteErrorException.class})
    @SuppressWarnings("unchecked")
    @Override
    public void logicDelete(PK id) throws LogicDeleteErrorException {
        try {
            assert poClass.getDeclaredField(DEL_FLAG_FIELD) != null;
        } catch (NoSuchFieldException e) {
            throw new LogicDeleteErrorException("逻辑删除失败，当前对象无" + DEL_FLAG_FIELD + "字段", e.getCause());
        }
        // 逻辑删除字段get方法
        String setter = "set" + StringUtil.capitalize(DEL_FLAG_FIELD);
        Method method;
        try {
            method = poClass.getMethod(setter, String.class);
        } catch (NoSuchMethodException e) {
            throw new LogicDeleteErrorException("逻辑删除失败，当前对象无" + setter + "(String)方法", e.getCause());
        }
        // 逻辑删除前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById((PK) model.getId());
        // 判断数据是否存在
        if (optional.isPresent()) {
            // 如果存在则将入参中的非null属性赋给查询结果
            try {
                method.invoke(optional.get(), DEL_FLAG_DELETE);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new LogicDeleteErrorException("逻辑删除失败，调用" + setter + "()方法失败", e.getCause());
            }
            baseRepository.save(optional.get());
        } else {
            throw new LogicDeleteErrorException("逻辑删除失败，未找到主键：" + model.getId() + " 对应数据");
        }
    }

    @Transactional(rollbackFor = {Exception.class, LogicDeleteErrorException.class})
    @Override
    public void logicDeleteAll(List<PK> ids) throws LogicDeleteErrorException {
        ids.forEach(this::logicDelete);
    }

    @Override
    public VO get(PK id) {
        Optional<PO> optional = baseRepository.findById(id);
        return optional.isPresent() ? dozerMapper.map(baseRepository.findById(id), voClass) : null;
    }

    @Override
    public List<VO> findAll() {
        return dozerMapper.mapList(baseRepository.findAll(), voClass);
    }

    @Override
    public List<VO> findAll(Sort sort) {
        return dozerMapper.mapList(baseRepository.findAll(sort), voClass);
    }

    @Override
    public List<VO> findAll(ExampleMatcher matcher) {
        Example<PO> example = Example.of(model, matcher);
        return dozerMapper.mapList(baseRepository.findAll(example), voClass);
    }

/*


    @Override
    public PageInfo<VO> listPage(int pageNum, int pageSize) {
        Page<VO> page = PageHelper.startPage(pageNum, pageSize);
        listAll();
        return new PageInfo<VO>(page);
    }

    @Override
    public PageInfo<VO> listPage(Example example, int pageNum, int pageSize) {
        Page<VO> page = PageHelper.startPage(pageNum, pageSize);
        list(example);
        return new PageInfo<VO>(page);
    }

    @Override
    public int count() {
        return count(new Example(poClass));
    }

    @Override
    public int count(Example example) {
        return baseRepository.selectCountByExample(example);
    }*/


}
