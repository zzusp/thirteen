package org.thirteen.authorization.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Aaron.Sun
 * @description 通用数据操作层接口，仅适用于单表操作（基于spring data jpa通用插件）
 * @date Created in 21:05 2018/1/10
 * @modified by
 */
@NoRepositoryBean
public interface BaseRepository<PO, PK> extends JpaRepository<PO, PK>, JpaSpecificationExecutor<PO> {
}
