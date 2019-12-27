package org.thirteen.authorization.service.impl.base;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.DataNotFoundException;
import org.thirteen.authorization.model.po.base.BasePO;
import org.thirteen.authorization.model.vo.base.BaseVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseService;
import org.thirteen.authorization.service.support.base.ModelInformation;
import org.thirteen.authorization.service.support.base.ModelSupport;

import java.lang.reflect.Method;
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
public abstract class BaseServiceImpl<VO extends BaseVO<PK>, PK, PO extends BasePO<PK>> implements BaseService<VO, PK> {

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
     * baseRepository注入
     */
    protected BaseRepository<PO, PK> baseRepository;
    /**
     * 对象转换器
     */
    protected DozerMapper dozerMapper;
    /**
     * PO对象信息
     */
    private ModelInformation<PO, PK> poInformation;
    /**
     * VO对象信息
     */
    private ModelInformation<VO, PK> voInformation;
    /**
     * PO实际class
     */
    private Class<PO> poClass;
    /**
     * PO实际class
     */
    private Class<VO> voClass;
    /**
     * PO对象帮助类
     */
    private ModelSupport<PO, PK> poSupport;

    public BaseServiceImpl(ModelInformation<PO, PK> poInformation, ModelInformation<VO, PK> voInformation) {
        this.poInformation = poInformation;
        this.voInformation = voInformation;
        this.poClass = this.poInformation.getRealClass();
        this.voClass = this.voInformation.getRealClass();
        this.poSupport = new ModelSupport<>(poInformation);
    }

    @Autowired
    public BaseServiceImpl(BaseRepository<PO, PK> baseRepository, DozerMapper dozerMapper) {
        this(new ModelInformation<>(), new ModelInformation<>());
        this.baseRepository = baseRepository;
        this.dozerMapper = dozerMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(VO model) {
        baseRepository.save(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAndFlush(VO model) {
        baseRepository.saveAndFlush(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAll(List<VO> models) {
        baseRepository.saveAll(dozerMapper.mapList(models, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(VO model) {
        baseRepository.save(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateSelective(VO model) {
        // 更新前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById(model.getId());
        // 判断数据是否存在
        if (optional.isPresent()) {
            // 如果存在则将入参中的非null属性赋给查询结果
            dozerMapper.copy(dozerMapper.map(model, poClass), optional.get(), false, true);
            baseRepository.save(optional.get());
        } else {
            throw new DataNotFoundException(model.getId());
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
    @Override
    public void deleteAll(List<PK> ids) {
        baseRepository.deleteAll(ids.stream().map(item -> poSupport.builder().id(item).build())
            .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<PK> ids) {
        baseRepository.deleteInBatch(ids.stream().map(item -> poSupport.builder().id(item).build())
            .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void logicDelete(PK id) {
        // 获取poClass中逻辑删除字段的set方法，获取不到则抛出异常
        Method method = poInformation.getSetter(DEL_FLAG_FIELD, String.class);
        // 逻辑删除前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById(id);
        // 判断数据是否存在
        if (optional.isPresent()) {
            // 如果存在则将入参中的非null属性赋给查询结果
            poInformation.invokeSet(method, optional.get(), DEL_FLAG_DELETE);
            baseRepository.save(optional.get());
        } else {
            throw new DataNotFoundException(id);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void logicDeleteAll(List<PK> ids) {
        ids.forEach(this::logicDelete);
    }

    @Override
    public VO get(PK id) {
        Optional<PO> optional = baseRepository.findById(id);
        return optional.map(po -> dozerMapper.map(po, voClass)).orElse(null);
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
        Example<PO> example = Example.of(poSupport.builder().build(), matcher);
        return dozerMapper.mapList(baseRepository.findAll(example), voClass);
    }



}
