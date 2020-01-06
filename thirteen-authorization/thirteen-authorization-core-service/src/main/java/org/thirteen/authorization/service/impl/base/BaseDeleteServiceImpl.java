package org.thirteen.authorization.service.impl.base;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.thirteen.authorization.dozer.DozerMapper;
import org.thirteen.authorization.exceptions.DataNotFoundException;
import org.thirteen.authorization.model.po.base.BaseDeletePO;
import org.thirteen.authorization.model.vo.base.BaseDeleteVO;
import org.thirteen.authorization.repository.base.BaseRepository;
import org.thirteen.authorization.service.base.BaseDeleteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.thirteen.authorization.service.support.base.ModelSupport.DEL_FLAG_DELETE;
import static org.thirteen.authorization.service.support.base.ModelSupport.DEL_FLAG_NORMAL;

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
        model.setDelFlag(DEL_FLAG_NORMAL);
        super.insert(model);
    }

    @Override
    public void insertAndFlush(VO model) {
        model.setDelFlag(DEL_FLAG_NORMAL);
        super.insertAndFlush(model);
    }

    @Override
    public void insertAll(List<VO> models) {
        models.forEach(item -> item.setDelFlag(DEL_FLAG_NORMAL));
        super.insertAll(models);
    }

    @Override
    public void update(VO model) {
        model.setDelFlag(DEL_FLAG_NORMAL);
        super.update(model);
    }

    @Override
    public void updateSelective(VO model) {
        model.setDelFlag(DEL_FLAG_NORMAL);
        super.updateSelective(model);
    }

    @Override
    public void updateAndFlush(VO model) {
        model.setDelFlag(DEL_FLAG_NORMAL);
        super.updateAndFlush(model);
    }

    @Override
    public void updateAll(List<VO> models) {
        models.forEach(item -> item.setDelFlag(DEL_FLAG_NORMAL));
        super.updateAll(models);
    }

    @Override
    public void delete(String id) {
        // 逻辑删除前先由ID获取数据信息
        Optional<PO> optional = baseRepository.findById(id);
        // 判断数据是否存在
        if (optional.isPresent()) {
            optional.get().setDelFlag(DEL_FLAG_DELETE);
            baseRepository.save(optional.get());
        } else {
            throw new DataNotFoundException(id);
        }
    }

    @Override
    public void deleteAll(List<String> ids) {
        super.deleteAll(ids);
    }

    @Override
    public void deleteInBatch(List<String> ids) {
        super.deleteInBatch(ids);
    }

    @Override
    public VO findById(String id) {
        VO model = super.findById(id);
        // 如果数据已被逻辑删除，则返回null
        if (model != null && !DEL_FLAG_NORMAL.equals(model.getDelFlag())) {
            model = null;
        }
        return model;
    }

    @Override
    public List<VO> findAll() {
        List<VO> models = super.findAll();
        return models.stream().filter(item -> !DEL_FLAG_NORMAL.equals(item.getDelFlag())).collect(Collectors.toList());
    }

    @Override
    public List<VO> findAll(Sort sort) {
        List<VO> models = super.findAll(sort);
        return models.stream().filter(item -> !DEL_FLAG_NORMAL.equals(item.getDelFlag())).collect(Collectors.toList());
    }

    @Override
    public List<VO> findAll(ExampleMatcher matcher) {
        List<VO> models = super.findAll(matcher);
        return models.stream().filter(item -> !DEL_FLAG_NORMAL.equals(item.getDelFlag())).collect(Collectors.toList());
    }
}
