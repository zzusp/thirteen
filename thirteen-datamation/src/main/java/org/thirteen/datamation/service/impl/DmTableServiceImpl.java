package org.thirteen.datamation.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmPage;
import org.thirteen.datamation.core.criteria.DmQuery;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.core.exception.DatamationException;
import org.thirteen.datamation.model.po.DmTablePO;
import org.thirteen.datamation.model.vo.DmTableVO;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;
import org.thirteen.datamation.service.DmTableService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.StringUtils;
import org.thirteen.datamation.web.PagerResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.thirteen.datamation.core.DmCodes.CODE;

/**
 * @author Aaron.Sun
 * @description 数据化表信息数据服务接口实现
 * @date Created in 16:36 2021/1/22
 * @modified by
 */
@Service
public class DmTableServiceImpl implements DmTableService {

    private final DmTableRepository dmTableRepository;
    private final DmColumnRepository dmColumnRepository;

    public DmTableServiceImpl(DmTableRepository dmTableRepository, DmColumnRepository dmColumnRepository) {
        this.dmTableRepository = dmTableRepository;
        this.dmColumnRepository = dmColumnRepository;
    }

    @Override
    public boolean isExist(String code) {
        PagerResult<DmTableVO> pagerResult = findAllBySpecification(DmSpecification.of().add(DmCriteria.equal(CODE, code)));
        return !pagerResult.isEmpty();
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    @Override
    public void insert(DmTableVO model) {
        String tableCode = model.getCode();
        if (StringUtils.isEmpty(tableCode)) {
            throw new DatamationException("编码不可为空");
        }
        if (isExist(tableCode)) {
            throw new DatamationException("对应编码的数据已存在，编码：" + tableCode);
        }
        model.setId(null);
        model.setCreateTime(LocalDateTime.now());
        // 保存table信息
        dmTableRepository.save(DmTablePO.convert(model, null));
        if (CollectionUtils.isNotEmpty(model.getColumns())) {
            model.getColumns().forEach(v -> {
                v.setId(null);
                v.setTableCode(tableCode);
                v.setCreateTime(LocalDateTime.now());
            });
            // 级联保存列信息
            dmColumnRepository.saveAll(model.getColumns());
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    @Override
    public void update(DmTableVO model) {
        // 查询当前的table信息
        Optional<DmTablePO> opt = dmTableRepository.findById(model.getId());
        if (opt.isEmpty()) {
            throw new DatamationException("修改失败，数据不存在或已被删除");
        }
        String tableCode = opt.get().getCode();
        // table编码禁止修改
        if (model.getCode() != null && !tableCode.equals(model.getCode())) {
            throw new DatamationException("修改失败，编码字段不可修改");
        }
        // 删除所有级联的列信息
        dmColumnRepository.deleteByTableCodeEquals(tableCode);
        model.setUpdateTime(LocalDateTime.now());
        dmTableRepository.save(DmTablePO.convert(model, opt.get()));
        if (CollectionUtils.isNotEmpty(model.getColumns())) {
            model.getColumns().forEach(v -> {
                v.setId(null);
                v.setTableCode(tableCode);
                v.setCreateTime(LocalDateTime.now());
            });
            // 级联保存列信息
            dmColumnRepository.saveAll(model.getColumns());
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // 查询当前的table信息
        Optional<DmTablePO> opt = dmTableRepository.findById(id);
        if (opt.isEmpty()) {
            throw new DatamationException("删除失败，数据不存在或已被删除");
        }
        // 删除所有级联的列信息
        dmColumnRepository.deleteByTableCodeEquals(opt.get().getCode());
        dmTableRepository.delete(opt.get());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(List<String> ids) {
        List<DmTablePO> pos = dmTableRepository.findAllById(ids);
        if (CollectionUtils.isEmpty(pos)) {
            throw new DatamationException("删除失败，数据不存在或已被删除");
        }
        // 删除所有级联的列信息
        dmColumnRepository.deleteByTableCodeIn(pos.stream().map(DmTablePO::getCode).collect(Collectors.toList()));
        dmTableRepository.deleteInBatch(pos);
    }

    @Override
    public DmTableVO findById(String id) {
        Optional<DmTablePO> opt = dmTableRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        DmTablePO po = opt.get();
        // 级联查询列信息
        po.setColumns(dmColumnRepository.findByTableCodeEquals(po.getCode(),
            Sort.by(Sort.Direction.ASC, "orderNumber")));
        return DmTableVO.convert(po);
    }

    @Override
    public PagerResult<DmTableVO> findAllBySpecification(DmSpecification dmSpecification) {
        Specification<DmTablePO> specification = null;
        Sort sort = null;
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
            Page<DmTablePO> page;
            // 判断排序参数是否不为空，如果不为空可与分页参数组合
            if (sort != null) {
                pageRequest = PageRequest.of(dmPage.getPageNum(), dmPage.getPageSize(), sort);
            } else {
                pageRequest = PageRequest.of(dmPage.getPageNum(), dmPage.getPageSize());
            }
            // 判断条件参数是否不为空，调用对应查询方法
            if (specification != null) {
                page = this.dmTableRepository.findAll(specification, pageRequest);
            } else {
                page = this.dmTableRepository.findAll(pageRequest);
            }
            // 处理查询结果，直接返回
            return PagerResult.of(page.getTotalElements(), DmTableVO.convert(page.getContent()));
        }

        // 判断条件参数与分页参数是否为空，调用对应的查询方法

        if (specification != null && sort != null) {
            return PagerResult.of(DmTableVO.convert(this.dmTableRepository.findAll(specification, sort)));
        }
        if (specification != null) {
            return PagerResult.of(DmTableVO.convert(this.dmTableRepository.findAll(specification)));
        }
        if (sort != null) {
            return PagerResult.of(DmTableVO.convert(this.dmTableRepository.findAll(sort)));
        }
        return PagerResult.of(DmTableVO.convert(this.dmTableRepository.findAll()));
    }
}
