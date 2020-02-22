package org.thirteen.authorization.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysDeptRolePO;
import org.thirteen.authorization.repository.base.BaseRepository;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 部门角色关联数据操作层接口
 * @date Created in 13:57 2020/2/22
 * @modified By
 */
@Repository
public interface SysDeptRoleRepository extends BaseRepository<SysDeptRolePO, String> {

    /**
     * 由部门编码删除部门角色关联
     *
     * @param deptCode 部门编码
     */
    @Modifying
    @Query("delete from SysDeptRolePO where deptCode = ?1")
    void deleteByDeptCode(String deptCode);

    /**
     * 由角色编码删除部门角色关联
     *
     * @param roleCode 角色编码
     */
    @Modifying
    @Query("delete from SysDeptRolePO where roleCode = ?1")
    void deleteByRoleCode(String roleCode);

    /**
     * 由部门编码获取部门角色关联集合
     *
     * @param deptCode 部门编码
     * @return 部门角色关联集合
     */
    List<SysDeptRolePO> findAllByDeptCode(String deptCode);

    /**
     * 由角色编码获取部门角色关联集合
     *
     * @param roleCode 角色编码
     * @return 部门角色关联集合
     */
    List<SysDeptRolePO> findAllByRoleCode(String roleCode);

}
