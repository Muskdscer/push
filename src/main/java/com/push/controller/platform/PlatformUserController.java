package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.webfacade.bo.platform.*;
import com.push.common.webfacade.vo.platform.PlatformUserInfoVO;
import com.push.entity.platform.PlatformUserInfo;
import com.push.service.platform.PlatformUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Description: 平台用户操作controller
 * Create DateTime: 2020-03-26 14:26
 *
 *

 */
@RestController
@RequestMapping("platformUser")
public class PlatformUserController extends BaseController {

    @Resource
    private PlatformUserService platformUserService;

    /**
     * 添加平台用户
     *
     * @param platformUserAddBO 添加平台用户BO
     * @return 添加结果
     */
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_USER_INFO, operationDescribe = "添加平台用户")
    @RequestMapping(value = "addPlatformUser", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> addPlatformUser(@RequestBody PlatformUserAddBO platformUserAddBO) {
        platformUserAddBO.validate();
        PlatformUserInfo platformUserInfo = new PlatformUserInfo();
        BeanUtils.copyProperties(platformUserAddBO, platformUserInfo);
        platformUserInfo.setCreateTime(new Date());
        platformUserService.save(platformUserInfo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 删除平台用户
     *
     * @param platformUserRemoveBO 删除平台用户BO
     * @return 删除结果
     */
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_USER_INFO, operationDescribe = "删除平台用户")
    @RequestMapping(value = "removePlatformUser", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> removePlatformUser(@RequestBody PlatformUserRemoveBO platformUserRemoveBO) {
        platformUserRemoveBO.validate();
        platformUserService.removeById(platformUserRemoveBO.getId());
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 修改平台用户
     *
     * @param platformUserModifyBO 修改平台用户BO
     * @return 修改结果
     */
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.PLATFORM_USER_INFO, operationDescribe = "修改平台用户")
    @RequestMapping(value = "modifyPlatformUser", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyPlatformUser(@RequestBody PlatformUserModifyBO platformUserModifyBO) {
        platformUserModifyBO.validate();
        PlatformUserInfo platformUserInfo = new PlatformUserInfo();
        BeanUtils.copyProperties(platformUserModifyBO, platformUserInfo);
        platformUserInfo.setUserName(null);
        platformUserService.updateById(platformUserInfo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    /**
     * 分页查询平台用户
     *
     * @param platformUserListBO 分页查询平台用户BO
     * @return 平台用户列表
     */
    @RequestMapping(value = "listPlatformUser", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listPlatformUser(@RequestBody PlatformUserListBO platformUserListBO) {
        platformUserListBO.validate();
        Page<PlatformUserInfoVO> list = platformUserService.queryPlatformUser(platformUserListBO.getPageNo(), platformUserListBO.getPageSize());
        list.getRecords().forEach(item -> {
            item.setPassword("***");
        });
        return returnResultMap(ResultMapInfo.GETSUCCESS, list);
    }

    /**
     * 分页查询平台用户
     *
     * @param platformUserDetailBO 查询平台用户详情BO
     * @return 平台用户列表
     */
    @RequestMapping(value = "getPlatformUserDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getPlatformUserDetail(@RequestBody PlatformUserDetailBO platformUserDetailBO) {
        platformUserDetailBO.validate();
        PlatformUserInfo platformUserInfo = platformUserService.getById(platformUserDetailBO.getId());
        platformUserInfo.setPassword("***");
        return returnResultMap(ResultMapInfo.GETSUCCESS, platformUserInfo);
    }

}
