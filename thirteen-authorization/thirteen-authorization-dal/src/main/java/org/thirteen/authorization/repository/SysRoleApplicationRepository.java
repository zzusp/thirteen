package org.thirteen.authorization.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysRoleApplicationPO;
import org.thirteen.authorization.repository.base.BaseRepository;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 角色应用关联数据操作层接口
 * @date Created in 13:58 2020/2/22
 * @modified By
 */
@Repository
public interface SysRoleApplicationRepository extends BaseRepository<SysRoleApplicationPO, String> {

    /**
     * 由角色编码删除角色应用关联
     *
     * @param roleCode 角色编码
     */
    @Modifying
    @Query("delete from SysRoleApplicationPO where roleCode = ?1")
    void deleteByRoleCode(String roleCode);

    /**
     * 由应用编码删除角色应用关联
     *
     * @param applicationCode 应用编码
     */
    @Modifying
    @Query("delete from SysRoleApplicationPO where applicationCode = ?1")
    void deleteByApplicationCode(String applicationCode);

    /**
     * 由角色编码集合获取角色下的应用信息集合
     *
     * @param roleCodes 角色编码集合
     * @return 角色下的应用信息集合
     */
    List<SysRoleApplicationPO> findAllByRoleCodeIn(List<String> roleCodes);

}
