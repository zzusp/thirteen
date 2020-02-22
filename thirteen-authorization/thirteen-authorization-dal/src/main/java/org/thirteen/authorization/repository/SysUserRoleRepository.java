package org.thirteen.authorization.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysUserRolePO;
import org.thirteen.authorization.repository.base.BaseRepository;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 用户角色关联数据操作层接口
 * @date Created in 13:58 2020/2/22
 * @modified By
 */
@Repository
public interface SysUserRoleRepository extends BaseRepository<SysUserRolePO, String> {

    /**
     * 由用户账号删除用户角色关联
     *
     * @param account 用户账号
     */
    @Modifying
    @Query("delete from SysUserRolePO where account = ?1")
    void deleteByAccount(String account);

    /**
     * 由角色编码删除用户角色关联
     *
     * @param roleCode 角色编码
     */
    @Modifying
    @Query("delete from SysUserRolePO where roleCode = ?1")
    void deleteByRoleCode(String roleCode);

    /**
     * 由用户账号获取用户角色关联集合
     *
     * @param account 用户账号
     * @return 用户角色关联集合
     */
    List<SysUserRolePO> findAllByAccount(String account);

    /**
     * 由角色编码获取用户角色关联集合
     *
     * @param roleCode 角色编码
     * @return 用户角色关联集合
     */
    List<SysUserRolePO> findAllByRoleCode(String roleCode);

}
