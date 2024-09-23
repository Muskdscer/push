package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.constants.RedisConstant;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.webfacade.bo.platform.*;
import com.push.entity.platform.RoleInfo;
import com.push.service.platform.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Description: 角色操作controller
 * Create DateTime: 2020-03-26 15:35
 *
 * 

 */
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 添加角色
     *
     * @param roleAddBO 添加角色BO
     * @return 添加结果
     */
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.ROLE, operationDescribe = "添加角色")
    @RequestMapping(value = "addRole", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> addRole(@RequestBody RoleAddBO roleAddBO) {
        roleAddBO.validate();
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleName(roleAddBO.getRoleName());
        roleService.save(roleInfo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 删除角色
     *
     * @param roleRemoveBO 删除角色BO
     * @return 删除结果
     */
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.ROLE, operationDescribe = "删除角色")
    @RequestMapping(value = "removeRole", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> removeRole(@RequestBody RoleRemoveBO roleRemoveBO) {
        roleRemoveBO.validate();
        roleService.removeById(roleRemoveBO.getId());
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 修改角色
     *
     * @param roleModifyBO 修改角色BO
     * @return 修改结果
     */
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.ROLE, operationDescribe = "修改角色")
    @RequestMapping(value = "modifyRole", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyRole(@RequestBody RoleModifyBO roleModifyBO) {
        roleModifyBO.validate();
        RoleInfo roleInfo = new RoleInfo();
        BeanUtils.copyProperties(roleModifyBO, roleInfo);
        roleService.updateById(roleInfo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 分页查询角色
     *
     * @param roleListBO 列表角色BO
     * @return 查询结果
     */
    @RequestMapping(value = "listRole", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listRole(@RequestBody RoleListBO roleListBO) {
        roleListBO.validate();
        Page<RoleInfo> page = roleService.queryRoleList(roleListBO.getPageNo(), roleListBO.getPageSize());
        return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }

    /**
     * 根据ID获取角色
     *
     * @param roleDetailBO 角色详情BO
     * @return 角色信息
     */
    @RequestMapping(value = "getRoleById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getRoleById(@RequestBody RoleDetailBO roleDetailBO) {
        roleDetailBO.validate();
        RoleInfo roleInfo = roleService.getById(roleDetailBO.getRoleId());
        return returnResultMap(ResultMapInfo.GETSUCCESS, roleInfo);
    }

    /**
     * 角色分配菜单
     *
     * @param distributeMenuBO 角色分配菜单BO
     * @return 分配结果
     */
    @CacheEvict(value = {RedisConstant.PUSH_ORDER_CACHE_CURRENT_ROLE_MENU_LIST}, key = "#distributeMenuBO.roleId", allEntries = true)
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.ROLE, operationDescribe = "为角色分配菜单")
    @RequestMapping(value = "distributeMenu", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> distributeMenu(@RequestBody DistributeMenuBO distributeMenuBO) {
        distributeMenuBO.validate();
        String menuIds = distributeMenuBO.getMenuIds();
        if (StringUtils.isNotBlank(menuIds)) {
            roleService.distributeMenu(distributeMenuBO.getRoleId(), Arrays.asList(menuIds.split(",")));
        } else {
            roleService.distributeMenu(distributeMenuBO.getRoleId(), Collections.emptyList());
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);

    }
}
