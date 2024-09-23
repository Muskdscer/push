package com.push.service.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.RoleInfo;

import java.util.List;

/**
 * Description: 角色service接口
 * Create DateTime: 2020-03-26 15:39
 *
 * 

 */
public interface RoleService extends IService<RoleInfo> {

    /**
     * 分页查询角色列表
     *
     * @param pageNo   页号
     * @param pageSize 页面大小
     * @return 角色列表
     */
    Page<RoleInfo> queryRoleList(Long pageNo, Long pageSize);

    /**
     * 分配菜单
     *
     * @param roleId  权限ID
     * @param menuIds 菜单ID集合
     */
    void distributeMenu(Long roleId, List<String> menuIds);
}
