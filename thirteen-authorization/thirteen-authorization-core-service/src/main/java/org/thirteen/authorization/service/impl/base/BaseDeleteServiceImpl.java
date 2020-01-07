package org.thirteen.authorization.service.impl.base;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.DataNotFoundException;
import org.thirteen.authorization.model.po.base.BaseDeletePO;
import org.thirteen.authorization.model.vo.base.BaseDeleteVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseDeleteService;
import org.thirteen.authorization.service.support.base.ModelInformation;
import org.thirteen.authorization.service.support.base.ModelSupport;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口实现类（实体类包含删除标记信息）
 * @date Created in 23:08 2020/1/6
 * @modified by
 */
public abstract class BaseDeleteServiceImpl<VO extends BaseDeleteVO, PO extends BaseDeletePO>
    extends BaseServiceImpl<VO, PO> implements BaseDeleteService<VO> {

    public BaseDeleteServiceImpl(BaseRepository<PO, String> baseRepository, DozerMapper dozerMapper) {
        super(baseRepository, dozerMapper);
    }

    @Override
    public void insert(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.save(dozerMapper.map(model, poClass).normal());
    }

    @Override
    public void insertAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.saveAndFlush(dozerMapper.map(model, poClass).normal());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        baseRepository.saveAll((List<PO>) dozerMapper.mapList(models, this.poClass).stream()
            .map(PO::normal).collect(Collectors.toList()));
    }

    @Override
    public void update(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.save(dozerMapper.map(model, poClass).normal());
    }

    @Override
    public void updateSelective(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        // 更新前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById(model.getId());
        // 判断数据是否存在
        if (optional.isPresent() && !optional.get().isDeleted()) {
            // 如果存在则将入参中的非null属性赋给查询结果
            dozerMapper.copy(dozerMapper.map(model, poClass), optional.get(), false, true);
            baseRepository.save(optional.get());
        } else {
            throw new DataNotFoundException(model.getId());
        }
    }

    @Override
    public void updateAndFlush(VO model) {
        Assert.notNull(model, "Entity must not be null!");
        baseRepository.saveAndFlush(dozerMapper.map(model, poClass).normal());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateAll(List<VO> models) {
        Assert.notEmpty(models, "Entity collection must not be empty!");
        baseRepository.saveAll((List<PO>) dozerMapper.mapList(models, this.poClass).stream()
            .map(PO::normal).collect(Collectors.toList()));
    }

    @Override
    public void delete(String id) {
        // 逻辑删除前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById(id);
        // 判断数据是否存在
        if (optional.isPresent() && !optional.get().isDeleted()) {
            baseRepository.save(optional.get().delete());
        } else {
            throw new DataNotFoundException(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteAll(List<String> ids) {
        Assert.notEmpty(ids, "ID collection must not be empty!");
        baseRepository.saveAll((List<PO>) ids.stream()
            .map(item -> baseRepository.findById(item).orElse(null))
            .filter(Objects::nonNull)
            .map(PO::delete)
            .collect(Collectors.toList()));
    }

    @Override
    public void deleteInBatch(List<String> ids) {
        super.deleteInBatch(ids);
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
