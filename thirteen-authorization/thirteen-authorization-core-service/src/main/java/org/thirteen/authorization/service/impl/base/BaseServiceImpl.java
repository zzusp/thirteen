package org.thirteen.authorization.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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

import static org.thirteen.authorization.service.support.base.ModelSupport.DEL_FLAG_DELETE;
import static org.thirteen.authorization.service.support.base.ModelSupport.DEL_FLAG_FIELD;

/**
 * @author Aaron.Sun
 * @description 通用Service层抽象类，仅适用于单表操作
 * @date Created in 21:43 2018/1/10
 * @modified by
 */
@Service
public abstract class BaseServiceImpl<VO extends BaseVO, PO extends BasePO> implements BaseService<VO> {

    /** baseRepository注入 */
    protected BaseRepository<PO, String> baseRepository;
    /** 对象转换器 */
    protected DozerMapper dozerMapper;
    /** PO对象信息 */
    private ModelInformation<PO> poInformation;
    /** VO对象信息 */
    private ModelInformation<VO> voInformation;
    /** PO实际class */
    private Class<PO> poClass;
    /** PO实际class */
    private Class<VO> voClass;
    /** PO对象帮助类 */
    private ModelSupport<PO> poSupport;

    public BaseServiceImpl() {
        this.poInformation = new ModelInformation<>();
        this.voInformation = new ModelInformation<>();
        this.poClass = this.poInformation.getRealClass();
        this.voClass = this.voInformation.getRealClass();
        this.poSupport = new ModelSupport<>(poInformation);
    }

    @Autowired
    public void setBaseRepository(BaseRepository<PO, String> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Autowired
    public void setDozerMapper(DozerMapper dozerMapper) {
        this.dozerMapper = dozerMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        PO po = dozerMapper.map(model, poClass);
        baseRepository.save(poSupport.getSaveModel(po, true));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        PO po = dozerMapper.map(model, poClass);
        baseRepository.saveAndFlush(poSupport.getSaveModel(po, true));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        List<PO> pos = dozerMapper.mapList(models, poClass);
        baseRepository.saveAll(poSupport.getSaveModels(pos, true));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        PO po = dozerMapper.map(model, poClass);
        baseRepository.save(poSupport.getSaveModel(po, false));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateSelective(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        // 更新前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById(model.getId());
        // 判断数据是否存在
        if (optional.isPresent()) {
            // 如果存在则将入参中的非null属性赋给查询结果
            dozerMapper.copy(dozerMapper.map(model, poClass), optional.get(), false, true);
            baseRepository.save(poSupport.getSaveModel(optional.get(), false));
        } else {
            throw new DataNotFoundException(model.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        PO po = dozerMapper.map(model, poClass);
        baseRepository.saveAndFlush(poSupport.getSaveModel(po, false));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        List<PO> pos = dozerMapper.mapList(models, poClass);
        baseRepository.saveAll(poSupport.getSaveModels(pos, false));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Assert.notNull(id, "The given id must not be null!");
        // 如果存在逻辑删除字段，则逻辑删除
        if (poInformation.contains(DEL_FLAG_FIELD)) {
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
        } else {
            baseRepository.deleteById(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        // 如果存在逻辑删除字段，则逻辑删除
        if (poInformation.contains(DEL_FLAG_FIELD)) {
            ids.forEach(this::delete);
        } else {
            baseRepository.deleteAll(ids.stream().map(item -> poSupport.builder().id(item).build())
                .collect(Collectors.toList()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        // 如果存在逻辑删除字段，则逻辑删除
        if (poInformation.contains(DEL_FLAG_FIELD)) {
            ids.forEach(this::delete);
        } else {
            baseRepository.deleteInBatch(ids.stream().map(item -> poSupport.builder().id(item).build())
                .collect(Collectors.toList()));
        }
    }

    @Override
    public VO get(String id) {
        Assert.notNull(id, "The given id must not be null!");
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
