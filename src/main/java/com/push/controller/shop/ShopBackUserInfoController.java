package com.push.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.shop.ModifyShopBackPasswordBO;
import com.push.common.webfacade.bo.shop.ModifyShopBackUserInfoBO;
import com.push.common.webfacade.vo.platform.ShopUserInfoVO;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020-04-07 15:33
 *
 *

 */
@RestController
@RequestMapping("back/shop")
public class ShopBackUserInfoController extends BaseController {

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @RequestMapping(value = "detailShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> detailShop() {
        LambdaQueryWrapper<ShopUserInfo> queryWrapper = new LambdaQueryWrapper<>(ShopUserInfo.class);
        queryWrapper.eq(ShopUserInfo::getId, getUserId()).eq(ShopUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode());
        ShopUserInfo shopUser = shopUserInfoService.getOne(queryWrapper);
        if (shopUser != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, BeanUtils.copyPropertiesChaining(shopUser, ShopUserInfoVO::new));
        }
        return returnResultMap(ResultMapInfo.GETFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.SHOP_USER, operationType = OperationTypeEnum.SHOP_USER_INFO, operationDescribe = "修改商户")
    @RequestMapping(value = "modifyShop", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyShop(@RequestBody ModifyShopBackUserInfoBO bo) {
        ShopUserInfo shopUser = BeanUtils.copyPropertiesChaining(bo, ShopUserInfo::new);
        shopUser.setId(getUserId());
        boolean update = shopUserInfoService.updateById(shopUser);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.SHOP_USER, operationType = OperationTypeEnum.SHOP_USER_INFO, operationDescribe = "修改商户密码")
    @RequestMapping(value = "modifyPwd", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> modifyPwd(@RequestBody ModifyShopBackPasswordBO bo) {
        ShopUserInfo shopUser = new ShopUserInfo();
        shopUser.setId(getUserId());
        shopUser.setPassword(bo.getNewPwd());
        boolean update = shopUserInfoService.updateById(shopUser);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }
}
