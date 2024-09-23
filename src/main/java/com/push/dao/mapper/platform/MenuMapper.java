package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.platform.MenuInfo;

import java.util.List;

/**
 * Description: 菜单Mapper
 * Create DateTime: 2020-03-26 09:10
 *
 * 

 */
public interface MenuMapper extends BaseMapper<MenuInfo> {

    /**
     * 根据角色ID获取菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuInfo> selectMenuByRoleId(Long roleId);

}
