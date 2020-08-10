package org.thirteen.datamation.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thirteen.datamation.DatamationAppilcation;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import java.time.LocalDateTime;

//@Transactional
//@Rollback
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatamationAppilcation.class)
public class DmColumnRepositoryTest {

    @Autowired
    private DmColumnRepository dmColumnRepository;

    @Test
    public void save() {
        DmColumnPO po = new DmColumnPO();
        po.setCode("id");
        DmTablePO table = new DmTablePO();
        table.setCode("rental_stock");
        table.setName("库存");
        table.setStatus((byte) 1);
        table.setCreateBy("admin");
        table.setCreateTime(LocalDateTime.now());
        table.setDelFlag((byte) 1);
        po.setTable(table);
        po.setName("主键");
        po.setJavaType("java.lang.String");
        po.setDbType("VARCHAR");
        po.setLength(32);
        po.setNotNull((byte) 1);
        po.setStatus((byte) 1);
        po.setCreateBy("admin");
        po.setCreateTime(LocalDateTime.now());
        po.setRemark("主键唯一");
        po.setDelFlag((byte) 1);
        dmColumnRepository.save(po);
    }

}
