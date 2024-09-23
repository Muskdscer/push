package com.push.controller.platform;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.*;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.ShopAgentMapVO;
import com.push.common.webfacade.vo.platform.ShopAgentUserDetailInfoVO;
import com.push.common.webfacade.vo.platform.ShopAgentUserInfoVO;
import com.push.common.webfacade.vo.platform.ShopUserInfoWaitMapVO;
import com.push.entity.agent.ShopAgentMapInfo;
import com.push.entity.agent.ShopAgentUserInfo;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.agent.ShopAgentMapInfoService;
import com.push.service.agent.ShopAgentUserInfoService;
import com.push.service.shop.ShopUserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户代理商信息表 前端控制器
 * </p>
 *
 *

 * @since 2020-04-26
 */
@RestController
@RequestMapping("shopAgent")
public class ShopAgentUserInfoController extends BaseController {

    @Resource
    private ShopAgentUserInfoService shopAgentUserInfoService;

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private ShopAgentMapInfoService shopAgentMapInfoService;

    //添加商户代理商
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "添加商户代理商")
    @RequestMapping("addShopAgent")
    public Map<String, Object> addShopAgent(@RequestBody ShopAgentUserInfoBO bo) {
        bo.validate();
        ShopAgentUserInfo shopAgentUserInfo = BeanUtils.copyPropertiesChaining(bo, ShopAgentUserInfo::new);
        List<ShopAgentUserInfo> list = shopAgentUserInfoService.list(Wrappers.lambdaQuery(ShopAgentUserInfo.class).eq(ShopAgentUserInfo::getUserName, bo.getUserName()));
        if (!list.isEmpty()) {
            return returnResultMap(ResultMapInfo.SHOP_AGENT_HAD_EXIST);
        }
        shopAgentUserInfoService.save(shopAgentUserInfo);
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    //修改商户代理商
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "修改商户代理商")
    @RequestMapping(value = "/modifyShopAgent", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyShopAgent(@RequestBody EditShopAgentUserInfoBO bo) {
        bo.validate();
        if (bo.getId() == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        ShopAgentUserInfo shopAgentUserInfo = BeanUtils.copyPropertiesChaining(bo, ShopAgentUserInfo::new);
        shopAgentUserInfo.setUserName(null);
        Boolean update = shopAgentUserInfoService.updateById(shopAgentUserInfo);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //删除商户代理商
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "删除商户代理商")
    @RequestMapping(value = "/removeShopAgent", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> removeShop(@RequestBody IdBO bo) {
        bo.validate();
        ShopAgentUserInfo info = new ShopAgentUserInfo();
        info.setId(bo.getId());
        info.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        Boolean update = shopAgentUserInfoService.updateById(info);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //查询商户代理商列表
    @RequestMapping(value = "/shopAgentList", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShopAgent(@RequestBody ShopAgentUserPageBO bo) {
        bo.validate();
        LambdaQueryWrapper<ShopAgentUserInfo> wrapper = Wrappers.lambdaQuery(ShopAgentUserInfo.class);
        if (!StringUtils.isBlank(bo.getUserName())) {
            wrapper.like(ShopAgentUserInfo::getUserName, bo.getUserName());
        }
        if (!StringUtils.isBlank(bo.getShopAgentName())) {
            wrapper.like(ShopAgentUserInfo::getShopAgentName, bo.getShopAgentName());
        }
        if (!StringUtils.isBlank(bo.getPhoneNumber())) {
            wrapper.like(ShopAgentUserInfo::getPhoneNumber, bo.getPhoneNumber());
        }
        if (bo.getStartTime() != null) {
            wrapper.ge(ShopAgentUserInfo::getCreateTime, bo.getStartTime());
        }
        if (bo.getEndTime() != null) {
            wrapper.le(ShopAgentUserInfo::getCreateTime, bo.getEndTime());
        }
        wrapper.eq(ShopAgentUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode());
        wrapper.orderByDesc(ShopAgentUserInfo::getCreateTime);
        Page<ShopAgentUserInfo> page = shopAgentUserInfoService.page(new Page<>(bo.getPageNo(), bo.getPageSize()), wrapper);
        List<ShopAgentUserInfo> records = page.getRecords();
        List<ShopAgentUserInfoVO> returnList = new ArrayList<>();
        records.forEach(shopAgentUserInfo -> {
            ShopAgentUserInfoVO shopAgentUserInfoVO = new ShopAgentUserInfoVO();
            shopAgentUserInfoVO.setBalance(shopAgentUserInfo.getBalance());
            shopAgentUserInfoVO.setId(shopAgentUserInfo.getId());
            shopAgentUserInfoVO.setCreateTime(shopAgentUserInfo.getCreateTime());
            shopAgentUserInfoVO.setFrozenMoney(shopAgentUserInfo.getFrozenMoney());
            shopAgentUserInfoVO.setUpdateTime(shopAgentUserInfo.getUpdateTime());
            shopAgentUserInfoVO.setPhoneNumber(shopAgentUserInfo.getPhoneNumber());
            shopAgentUserInfoVO.setUserName(shopAgentUserInfo.getUserName());
            shopAgentUserInfoVO.setShopAgentName(shopAgentUserInfo.getShopAgentName());
            returnList.add(shopAgentUserInfoVO);
        });
        Page<ShopAgentUserInfoVO> shopAgentUserInfoVOPage = new Page<>();
        shopAgentUserInfoVOPage.setTotal(page.getTotal()).setRecords(returnList);
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopAgentUserInfoVOPage);
    }

    //查询所有商户
    @RequestMapping(value = "/listShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShop() {
        //商户总列表
        List<ShopUserInfo> shopUserInfoList = shopUserInfoService.list();
        List<ShopUserInfoWaitMapVO> shopUserInfoWaitMapVOS = new ArrayList<>();
        shopUserInfoList.forEach(shopUserInfo -> {
            ShopUserInfoWaitMapVO shopUserInfoWaitMapVO = new ShopUserInfoWaitMapVO();
            shopUserInfoWaitMapVO.setId(shopUserInfo.getId());
            shopUserInfoWaitMapVO.setShopName(shopUserInfo.getShopName());
            shopUserInfoWaitMapVOS.add(shopUserInfoWaitMapVO);
        });
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopUserInfoWaitMapVOS);
    }

    //查询被代理商户
    @RequestMapping(value = "/shopAgentMap", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> shopAgentMap(@RequestBody ShopAgentMapBO bo) {
        bo.validate();
        Page<ShopAgentMapVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        //被代理商户列表
        Page<ShopAgentMapVO> shopAgentMapVoPage = shopAgentUserInfoService.getShopAgentMap(page, bo.getId());
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopAgentMapVoPage);
    }

    //代理商增加商户
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "平台为商户代理商绑定商户")
    @RequestMapping(value = "/addShopAgentMap", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> addShopAgentMap(@RequestBody AddShopAgentMapBO bo) {
        bo.validate();
        ShopAgentMapInfo shopAgentMapInfo = shopAgentMapInfoService.getOne(Wrappers.lambdaQuery(ShopAgentMapInfo.class)
                .eq(ShopAgentMapInfo::getShopId, bo.getShopId())
                .eq(ShopAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        ShopAgentMapInfo saveShopAgentMapInfo = BeanUtils.copyPropertiesChaining(bo, ShopAgentMapInfo::new);
        if (shopAgentMapInfo != null) {
            if (shopAgentMapInfo.getDeleteFlag() == DeleteFlagEnum.NOT_DELETE.getCode()) {
                return returnResultMap(ResultMapInfo.SHOP_HAD_AGENT);
            }
        }
        shopAgentMapInfoService.save(saveShopAgentMapInfo);
        return returnResultMap(ResultMapInfo.GETSUCCESS);
    }

    //编辑费率
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "平台修改商户代理商对应商户的费率")
    @RequestMapping(value = "/modifyShopAgentMap", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyShopAgentMap(@RequestBody EditShopAgentMapBO bo) {
        bo.validate();
        ShopAgentMapInfo shopAgentMapInfo = shopAgentMapInfoService.getById(bo.getMapId());
        shopAgentMapInfo.setRate(bo.getRate());
        boolean update = shopAgentMapInfoService.updateById(shopAgentMapInfo);
        if (shopAgentMapInfo == null || !update) {
            return returnResultMap(ResultMapInfo.EDITFAIL);
        }
        return returnResultMap(ResultMapInfo.GETSUCCESS);
    }

    //删除绑定关系
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.SHOP_AGENT_USER_INFO, operationDescribe = "平台删除代理商和绑定商户关系")
    @RequestMapping(value = "/removeShopAgentMap", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> removeShopAgentMap(@RequestBody IdBO bo) {
        bo.validate();
        ShopAgentMapInfo shopAgentMapInfo = new ShopAgentMapInfo();
        shopAgentMapInfo.setId(bo.getId());
        shopAgentMapInfo.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        boolean update = shopAgentMapInfoService.updateById(shopAgentMapInfo);
        if (!update) {
            return returnResultMap(ResultMapInfo.DELETEFAIL);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    //商户代理商详情
    @PostMapping("/detailShopAgent")
    public Map<String, Object> detailUpstream(@RequestBody IdBO bo) {
        bo.validate();
        ShopAgentUserInfo userInfo = shopAgentUserInfoService.getById(bo.getId());
        if (userInfo == null || DeleteFlagEnum.DELETE.getCode() == userInfo.getDeleteFlag()) {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
        ShopAgentUserDetailInfoVO shopAgentUserDetailInfoVO = BeanUtils.copyPropertiesChaining(userInfo, ShopAgentUserDetailInfoVO::new);
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopAgentUserDetailInfoVO);
    }

}
