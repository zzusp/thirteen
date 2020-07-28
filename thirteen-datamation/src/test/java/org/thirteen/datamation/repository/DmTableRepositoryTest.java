package org.thirteen.datamation.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.thirteen.datamation.DatamationAppilcation;
import org.thirteen.datamation.model.po.DmTablePO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Rollback
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatamationAppilcation.class)
public class DmTableRepositoryTest {

    @Autowired
    private DmTableRepository dmTableRepository;

    @Test
    public void save() {
        DmTablePO po = new DmTablePO();
        po.setCode("rental_stock");
        po.setName("库存");
        po.setStatus((byte) 1);
        po.setCreateBy("admin");
        po.setCreateTime(LocalDateTime.now());
        po.setDelFlag((byte) 1);
        dmTableRepository.save(po);
    }

    @Test
    public void update() {
        Specification<DmTablePO> specification = (Specification<DmTablePO>)
            (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("code"), "rental_stock");
        Optional<DmTablePO> po = dmTableRepository.findOne(specification);
        po.ifPresent(dmTablePO -> {
            dmTablePO.setUpdateBy("admin");
            dmTablePO.setUpdateTime(LocalDateTime.now());
            dmTableRepository.save(dmTablePO);
        });
    }

    @Test
    public void delete() {
        Specification<DmTablePO> specification = (Specification<DmTablePO>)
            (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("code"), "rental_stock");
        Optional<DmTablePO> po = dmTableRepository.findOne(specification);
        po.ifPresent(dmTablePO -> dmTableRepository.delete(dmTablePO));
    }

    @Test
    public void findAll() {
        List<DmTablePO> list = dmTableRepository.findAll();
        for (DmTablePO po : list) {
            System.out.println(po.toString());
        }
    }

}
