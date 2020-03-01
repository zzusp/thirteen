package org.thirteen.authorization.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysRolePermissionPO;
import org.thirteen.authorization.repository.base.BaseRepository;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 角色权限关联数据操作层接口
 * @date Created in 13:58 2020/2/22
 * @modified By
 */
@Repository
public interface SysRolePermissionRepository extends BaseRepository<SysRolePermissionPO, String> {

    /**
     * 由角色编码删除角色权限关联
     *
     * @param roleCode 角色编码
     */
    @Modifying
    @Query("delete from SysRolePermissionPO where roleCode = ?1")
    void deleteByRoleCode(String roleCode);

    /**
     * 由权限编码删除角色权限关联
     *
     * @param permissionCode 权限编码
     */
    @Modifying
    @Query("delete from SysRolePermissionPO where permissionCode = ?1")
    void deleteByPermissionCode(String permissionCode);

    /**
     * 由角色编码集合获取角色下的权限信息集合
     *
     * @param roleCodes 角色编码集合
     * @return 角色下的权限信息集合
     */
    List<SysRolePermissionPO> findAllByRoleCodeIn(List<String> roleCodes);
}
