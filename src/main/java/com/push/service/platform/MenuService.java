package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.MenuInfo;

import java.util.List;

/**
 * Description: 菜单service接口
 * Create DateTime: 2020-03-25 18:14
 *
 * 

 */
public interface MenuService extends IService<MenuInfo> {

    /**
     * 根据角色ID获取指定菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuInfo> queryMenuByRoleId(Long roleId);

}
