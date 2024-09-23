package com.push.controller.platform;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.webfacade.bo.platform.RoleMenuBO;
import com.push.common.webfacade.vo.platform.MenuVO;
import com.push.entity.platform.MenuInfo;
import com.push.entity.platform.RoleMenu;
import com.push.service.platform.MenuService;
import com.push.service.platform.RoleMenuService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 菜单Controller
 * Create DateTime: 2020-03-25 18:11
 *
 *

 */
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Resource
    private MenuService menuService;

    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 获取角色对应的菜单列表
     *
     * @return 菜单列表
     */
    @Cacheable(value = RedisConstant.PUSH_ORDER_CACHE_CURRENT_ROLE_MENU_LIST, key = "#roleMenuBO.roleId",
            condition = "#roleMenuBO.roleId != null", unless = "#result.get('code') == 2")
    @RequestMapping(value = "getMenuList", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getMenuList(@RequestBody RoleMenuBO roleMenuBO) {
        //Long roleId = getRoleId();
        List<MenuInfo> menuInfoList = menuService.queryMenuByRoleId(roleMenuBO.getRoleId());
        return returnResultMap(ResultMapInfo.GETSUCCESS, menuInfoList);
    }

    /**
     * 根据角色ID获取菜单列表
     *
     * @param roleMenuBO 角色菜单BO
     * @return 菜单列表
     */
    @RequestMapping(value = "getMenuByRoleId", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getMenuByRole(@RequestBody RoleMenuBO roleMenuBO) {
        roleMenuBO.validate();
        List<MenuInfo> menuInfos = menuService.queryMenuByRoleId(roleMenuBO.getRoleId());
        return returnResultMap(ResultMapInfo.GETSUCCESS, menuInfos);
    }

    /**
     * 获取所有的菜单
     *
     * @return 所有的菜单
     */
    @RequestMapping(value = "getAllMenu", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getAllMenu() {
        List<MenuInfo> list = menuService.list();

        List<MenuInfo> parentMenuList = list.stream().filter(menuInfo -> menuInfo.getParentId() == 0).collect(Collectors.toList());
        List<MenuInfo> childrenMenuList = list.stream().filter(menuInfo -> menuInfo.getParentId() != 0).collect(Collectors.toList());

        List<MenuVO> menuVOList = new ArrayList<>();

        parentMenuList.forEach(item -> {
            List<MenuInfo> menuInfoList = childrenMenuList.stream().filter(menuInfo -> menuInfo.getParentId().equals(item.getId())).collect(Collectors.toList());
            List<MenuVO> childrenMenuVO = menuInfoList.stream().map(menuInfo -> new MenuVO(menuInfo.getId().toString(), menuInfo.getName())).collect(Collectors.toList());
            MenuVO menuVO = new MenuVO(item.getId().toString(), item.getName(), childrenMenuVO);
            menuVOList.add(menuVO);
        });
        return returnResultMap(ResultMapInfo.GETSUCCESS, menuVOList);
    }

    /**
     * 根据角色ID获取所有已绑定的菜单ID
     *
     * @param roleMenuBO 角色菜单BO
     * @return 所有已绑定的菜单ID
     */
    @RequestMapping(value = "getMenuIdsListByRoleId", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getMenuIdsListByRoleId(@RequestBody RoleMenuBO roleMenuBO) {
        roleMenuBO.validate();

        List<RoleMenu> roleMenuList = roleMenuService.list(Wrappers.lambdaQuery(RoleMenu.class)
                .eq(RoleMenu::getRoleId, roleMenuBO.getRoleId())
                .eq(RoleMenu::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));

        List<String> menuIds = roleMenuList.stream().map(item -> String.valueOf(item.getMenuId())).collect(Collectors.toList());

        return returnResultMap(ResultMapInfo.GETSUCCESS, menuIds);
    }
}
