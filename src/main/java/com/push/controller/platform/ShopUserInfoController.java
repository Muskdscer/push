package com.push.controller.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.*;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.ModifyShopUserInfoBO;
import com.push.common.webfacade.bo.platform.SaveShopUserInfoBO;
import com.push.common.webfacade.bo.platform.ShopUserPageBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.ShopUserInfoVO;
import com.push.entity.shop.ShopPushMatch;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopPushMatchService;
import com.push.service.shop.ShopUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/3/27 18:10
 *
 * 

 */
@RestController
@RequestMapping("shop")
public class ShopUserInfoController extends BaseController {

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private ShopPushMatchService shopPushMatchService;


    //添加商户
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_USER_INFO, operationDescribe = "添加商户")
    @RequestMapping(value = "/addShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> addShop(@RequestBody SaveShopUserInfoBO bo) {
        bo.validate();
        ShopUserInfo shopUser = BeanUtils.copyPropertiesChaining(bo, ShopUserInfo::new);
        ShopUserInfo user1 = shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getAppId, bo.getAppId())
                .eq(ShopUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        ShopUserInfo user = shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getUserName, bo.getUserName())
                .eq(ShopUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        if (user != null || user1 != null) {
            return returnResultMap(ResultMapInfo.SHOP_HAD_EXIST);
        }
        //
        List<ShopPushMatch> shopPushMatches = new ArrayList<>();
        MatchMoneyEnum[] moneys = MatchMoneyEnum.values();
        OperatorTypeEnum[] opes = OperatorTypeEnum.values();
        int ope = opes.length;
        for (int i = 0; i < ope; i++) {
            for (MatchMoneyEnum matchMoneyEnum : moneys) {
                ShopPushMatch shopPushMatch = new ShopPushMatch();
                shopPushMatch.setOperatorType(opes[i].getCode()).setMoney(matchMoneyEnum.getMoney()).setMatchClassifyId(bo.getMatchClassifyId());
                if (i == 0) {
                    shopPushMatches.add(shopPushMatch);
                } else if (i == 1) {
                    shopPushMatches.add(shopPushMatch);
                } else {
                    shopPushMatches.add(shopPushMatch);
                }
            }
        }

        boolean save = shopUserInfoService.save(shopUser);
        if (!save){
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }
        ShopUserInfo userInfo = shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getUserName, bo.getUserName())
                .eq(ShopUserInfo::getDeleteFlag,DeleteFlagEnum.NOT_DELETE.getCode()));
        List<ShopPushMatch> collect = shopPushMatches.stream().map(s -> s.setShopId(userInfo.getId())).collect(Collectors.toList());
        boolean saveShopPushMatch = shopPushMatchService.saveBatch(collect);
        if (saveShopPushMatch) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //修改商户
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_USER_INFO, operationDescribe = "修改商户")
    @RequestMapping(value = "/modifyShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyShop(@RequestBody ModifyShopUserInfoBO bo) {
        bo.validate();
        if (bo.getId() == null) {
            throw new CommonException(ErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        ShopUserInfo user = shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getAppId, bo.getAppId())
                .eq(ShopUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        if (user != null && !user.getId().equals(bo.getId())){
            return returnResultMap(ResultMapInfo.APPID_HAD_EXIST);
        }
        Boolean update = shopUserInfoService.updateAndPushMatchs(bo);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //删除商户
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_USER_INFO, operationDescribe = "删除商户")
    @RequestMapping(value = "/removeShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> removeShop(@RequestBody IdBO bo) {
        bo.validate();
        ShopUserInfo info = new ShopUserInfo();
        info.setId(bo.getId());
        info.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        ShopPushMatch shopPushMatch = new ShopPushMatch();
        shopPushMatch.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        boolean result = shopUserInfoService.deleteUserAndMatch(shopPushMatch,info);
        if (result) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }


    //查询商户详情
    @RequestMapping(value = "/detailShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> detailShop(@RequestBody IdBO bo) {
        bo.validate();
        ShopUserInfoVO shopUserInfoVO = shopUserInfoService.getByIdWithMatch(bo.getId(), DeleteFlagEnum.NOT_DELETE.getCode());
        if (shopUserInfoVO != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, shopUserInfoVO);
        }
        return returnResultMap(ResultMapInfo.GETFAIL);
    }

    //查询加分页
    @RequestMapping(value = "/listShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listShop(@RequestBody ShopUserPageBO bo) {
        bo.validate();
        IPage<ShopUserInfoVO> returnList = shopUserInfoService.pageWithMatch(bo);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", returnList.getRecords());
        returnMap.put("total", returnList.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }
}
