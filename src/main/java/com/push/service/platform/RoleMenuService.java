package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.RoleMenu;

import java.util.Set;

/**
 * Description:
 * Create DateTime: 2020-04-17 13:29
 *
 * 

 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 根据角色ID查询菜单下的接口uri
     *
     * @param roleId 角色ID
     * @return 菜单下的接口uri
     */
    Set<String> queryMenuInterfaceUriByRoleId(Long roleId);
}
