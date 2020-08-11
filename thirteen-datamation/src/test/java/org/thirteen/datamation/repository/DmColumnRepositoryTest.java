package org.thirteen.datamation.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.thirteen.datamation.DatamationAppilcation;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Rollback
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatamationAppilcation.class)
public class DmColumnRepositoryTest {

    @Autowired
    private DmTableRepository dmTableRepository;
    @Autowired
    private DmColumnRepository dmColumnRepository;

    @Test
    public void save() {
        DmColumnPO po = new DmColumnPO();
        po.setTableCode("rental_stock");
        po.setCode("test_field");
        po.setName("测试字段");
        po.setJavaType("java.lang.Integer");
        po.setDbType("INT");
        po.setLength(null);
        po.setNotNull((byte) 1);
        po.setStatus((byte) 1);
        po.setCreateBy("admin");
        po.setCreateTime(LocalDateTime.now());
        po.setRemark(null);
        po.setDelFlag((byte) 1);
        dmColumnRepository.save(po);
    }

}
