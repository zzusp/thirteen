package org.thirteen.authorization.service.impl.base;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description 通用Service层抽象类，仅适用于单表操作
 * @date Created in 21:43 2018/1/10
 * @modified by
 */
public abstract class BaseServiceImpl<VO extends BaseVO, PO extends BasePO> implements BaseService<VO> {

    /** baseRepository */
    protected BaseRepository<PO, String> baseRepository;
    /** 对象转换器 */
    protected DozerMapper dozerMapper;
    /** PO对象信息 */
    protected ModelInformation<PO> poInformation;
    /** VO对象信息 */
    protected ModelInformation<VO> voInformation;
    /** PO实际class */
    protected Class<PO> poClass;
    /** PO实际class */
    protected Class<VO> voClass;
    /** PO对象帮助类 */
    protected ModelSupport<PO> poSupport;

    BaseServiceImpl(BaseRepository<PO, String> baseRepository, DozerMapper dozerMapper) {
        this.poInformation = new ModelInformation<>();
        this.voInformation = new ModelInformation<>();
        this.poClass = this.poInformation.getRealClass();
        this.voClass = this.voInformation.getRealClass();
        this.poSupport = new ModelSupport<>(poInformation);
        this.baseRepository = baseRepository;
        this.dozerMapper = dozerMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.save(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.saveAndFlush(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        baseRepository.saveAll(dozerMapper.mapList(models, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.save(dozerMapper.map(model, poClass));
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
            baseRepository.save(optional.get());
        } else {
            throw new DataNotFoundException(model.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.saveAndFlush(dozerMapper.map(model, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        baseRepository.saveAll(dozerMapper.mapList(models, poClass));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Assert.notNull(id, "The given id must not be null!");
        baseRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        baseRepository.deleteAll(ids.stream().map(item -> baseRepository.findById(item).orElse(null))
            .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        // 如果存在逻辑删除字段，则逻辑删除
        baseRepository.deleteInBatch(ids.stream().map(item -> baseRepository.findById(item).orElse(null))
            .collect(Collectors.toList()));
    }

    @Override
    public VO findById(String id) {
        Assert.notNull(id, "The given id must not be null!");
        Optional<PO> optional = baseRepository.findById(id);
        return optional.map(po -> dozerMapper.map(po, voClass)).orElse(null);
    }

    @Override
    public List<VO> findByIds(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        return ids.stream()
            .map(item -> baseRepository.findById(item).map(model -> dozerMapper.map(model, voClass)).orElse(null))
            .collect(Collectors.toList());
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
        Example<PO> example = Example.of(poSupport.newInstance(), matcher);
        return dozerMapper.mapList(baseRepository.findAll(example), voClass);
    }
}
