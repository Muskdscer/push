package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.platform.MenuMapper;
import com.push.entity.platform.MenuInfo;
import com.push.service.platform.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 菜单service接口实现类
 * Create DateTime: 2020-03-25 18:15
 *
 *

 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuInfo> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<MenuInfo> queryMenuByRoleId(Long roleId) {
        List<MenuInfo> menuInfoList = menuMapper.selectMenuByRoleId(roleId);
        List<MenuInfo> parentMenu = menuInfoList.stream()
                .filter(menuInfo -> menuInfo.getParentId() == 0)
                .collect(Collectors.toList());

        List<MenuInfo> childrenMenu = menuInfoList.stream().filter(menuInfo -> menuInfo.getParentId() != 0)
                .collect(Collectors.toList());

        parentMenu.forEach(item -> {
            List<MenuInfo> childrenMenuList = childrenMenu.stream().filter(menuInfo -> menuInfo.getParentId().equals(item.getId()))
                    .collect(Collectors.toList());
            item.setChildren(childrenMenuList);
        });
        return parentMenu;
    }
}
