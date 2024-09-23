package com.push.service.platform.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.dao.mapper.platform.RoleMapper;
import com.push.dao.mapper.platform.RoleMenuMapper;
import com.push.dao.platform.RoleDao;
import com.push.entity.platform.RoleInfo;
import com.push.entity.platform.RoleMenu;
import com.push.service.platform.RoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 角色service接口实现类
 * Create DateTime: 2020-03-26 15:40
 *
 *

 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleInfo> implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public Page<RoleInfo> queryRoleList(Long pageNo, Long pageSize) {
        return roleDao.selectRoleList(pageNo, pageSize);
    }

    @Override
    @TransactionalWithRollback
    public void distributeMenu(Long roleId, List<String> menuIds) {
        roleMenuMapper.delete(Wrappers.lambdaUpdate(RoleMenu.class).eq(RoleMenu::getRoleId, roleId));
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<RoleMenu> roleMenuList = menuIds.stream().map(menuId -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(Long.valueOf(menuId));
                return roleMenu;
            }).collect(Collectors.toList());
            roleMenuMapper.batchInsert(roleMenuList);
        }
    }
}
