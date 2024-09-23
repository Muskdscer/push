package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.platform.MenuInfo;
import com.push.entity.platform.RoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 角色菜单Mapper
 * Create DateTime: 2020-03-26 16:11
 *
 * 

 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 批量插入角色菜单关联关系
     *
     * @param roleMenuList 角色菜单集合
     * @return 受影响的行数
     */
    int batchInsert(@Param("roleMenuList") List<RoleMenu> roleMenuList);

    /**
     * 根据角色ID获取菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuInfo> selectMenuByRoleId(@Param("roleId") Long roleId);
}
