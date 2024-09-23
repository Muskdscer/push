package com.push.controller.shop;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.ShopExtractionRecordBO;
import com.push.common.webfacade.bo.shop.ShopBackExtractionBO;
import com.push.common.webfacade.vo.platform.GetExtractionVO;
import com.push.common.webfacade.vo.platform.ShopCheckExtractionVO;
import com.push.entity.shop.ShopUserExtraction;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopUserExtractionService;
import com.push.service.shop.ShopUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020-04-07 16:18
 *
 *

 */
@RestController
@RequestMapping("back/shopExtraction")
public class ShopBackExtractionController extends BaseController {

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private ShopUserExtractionService shopUserExtractionService;

    @Operation(userType = UserTypeCodeEnum.SHOP_USER, operationType = OperationTypeEnum.SHOP_EXTRACTION, operationDescribe = "商户发起商户提现")
    @RequestMapping(value = "/extraction", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> extraction(@RequestBody ShopBackExtractionBO bo) {
        bo.validate();
        Long userId = getUserId();
        ShopUserInfo shopUserInfo = shopUserInfoService.getById(userId);
        ShopUserExtraction shopUserExtraction = BeanUtils.copyPropertiesChaining(bo, ShopUserExtraction::new);
        shopUserExtraction.setShopUserId(userId);
        shopUserExtraction.setExtractionNumber(TokenUtil.generateOrderNo());
        //判断提现的金额是否不足
        if (shopUserExtraction.getExtractionMoney().compareTo(shopUserInfo.getBalance().subtract(shopUserInfo.getFrozenMoney())) == 1) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        shopUserExtraction.setUserMobile(shopUserInfo.getPhoneNumber());
        shopUserInfo.setFrozenMoney(shopUserInfo.getFrozenMoney().add(shopUserExtraction.getExtractionMoney()));
        boolean save = shopUserExtractionService.saveExtractionAndUpdateUserInfo(shopUserExtraction, shopUserInfo);
        if (save) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> shopExtractionToDoList(@RequestBody ShopExtractionRecordBO bo) {
        bo.setShopUserId(getUserId());
        Page<ShopCheckExtractionVO> page = shopUserExtractionService.getShopExtractionToDoList(ExtractionStatusEnum.TODO.getCode(), bo);
        if (page != null) {
            GetExtractionVO getExtractionVO = new GetExtractionVO();
            getExtractionVO.setData(page.getRecords());
            getExtractionVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getExtractionVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //提现成功记录
    @RequestMapping(value = "listExtractionSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopExtractionSuccessList(@RequestBody ShopExtractionRecordBO bo) {
        bo.setShopUserId(getUserId());
        Page<ShopCheckExtractionVO> page = shopUserExtractionService.getShopExtractionToDoList(ExtractionStatusEnum.SUCCESSS.getCode(), bo);
        if (page != null) {
            GetExtractionVO getExtractionVO = new GetExtractionVO();
            getExtractionVO.setData(page.getRecords());
            getExtractionVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getExtractionVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    //提现失败记录
    @RequestMapping(value = "listExtractionFailed", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopExtractionFailList(@RequestBody ShopExtractionRecordBO bo) {
        bo.setShopUserId(getUserId());
        Page<ShopCheckExtractionVO> page = shopUserExtractionService.getShopExtractionToDoList(ExtractionStatusEnum.FAIL.getCode(), bo);
        if (page != null) {
            GetExtractionVO getExtractionVO = new GetExtractionVO();
            getExtractionVO.setData(page.getRecords());
            getExtractionVO.setTotal(page.getTotal());
            return returnResultMap(ResultMapInfo.GETSUCCESS, getExtractionVO);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

}
