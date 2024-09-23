package com.push.controller.platform;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.DateUtil;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.ShopExtractionBO;
import com.push.common.webfacade.bo.platform.ShopExtractionRecordBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.GetExtractionVO;
import com.push.common.webfacade.vo.platform.ShopCheckExtractionVO;
import com.push.entity.shop.ShopUserExtraction;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopUserExtractionService;
import com.push.service.shop.ShopUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Description: 商户提现controller
 * Create DateTime: 2020-03-30 16:54
 *
 * 

 */
@RestController
@Slf4j
@RequestMapping("shopExtraction")
public class ShopExtractionController extends BaseController {

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private ShopUserExtractionService shopUserExtractionService;

    //商户提现
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_EXTRACTION, operationDescribe = "平台发起商户提现")
    @RequestMapping(value = "/extraction", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> extraction(@RequestBody ShopExtractionBO bo) {
        bo.validate();
        if (bo.getExtractionMoney().compareTo(new BigDecimal(0)) <= 0) {
            return returnResultMap(ResultMapInfo.MINEXTRACTIONFAIL);
        }
        ShopUserInfo shopUserInfo = shopUserInfoService.getById(bo.getId());
        if (shopUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        ShopUserExtraction shopUserExtraction = BeanUtils.copyPropertiesChaining(bo, ShopUserExtraction::new);
        shopUserExtraction.setShopUserId(bo.getId());
        shopUserExtraction.setExtractionNumber(TokenUtil.generateOrderNo());
        //判断提现的金额是否不足
        if (shopUserExtraction.getExtractionMoney().compareTo(shopUserInfo.getBalance().subtract(shopUserInfo.getFrozenMoney())) > 0) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        //更改商户用户表冻结金额
        shopUserInfo.setFrozenMoney(shopUserInfo.getFrozenMoney().add(shopUserExtraction.getExtractionMoney()));
        boolean save = shopUserExtractionService.saveExtractionAndUpdateUserInfo(shopUserExtraction, shopUserInfo);
        if (save) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //待处理提现记录
    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> shopExtractionToDoList(@RequestBody ShopExtractionRecordBO bo) {
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

    //审核成功
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_EXTRACTION, operationDescribe = "商户提现审核成功")
    @RequestMapping(value = "passCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> successShopUserExtraction(@RequestBody IdBO bo) {
        ShopUserExtraction shopUserExtraction = shopUserExtractionService.getById(bo.getId());
        if (null == shopUserExtraction) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        ShopUserInfo shopUserInfo = shopUserInfoService.getById(shopUserExtraction.getShopUserId());
        if (null == shopUserInfo) {
            return returnResultMap(ResultMapInfo.SHOP_NOT_EXIST);
        }
        if (shopUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        //判断余额是否充足
        if (shopUserInfo.getBalance().compareTo(shopUserInfo.getFrozenMoney()) < 0) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        } else {
            //更改提现提现订单状态，增加账单事务表记录,增加账单表
            shopUserExtraction.setStatus(ExtractionStatusEnum.SUCCESSS.getCode());
            shopUserExtractionService.operatorSuccess(shopUserExtraction, shopUserInfo);
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
    }

    //审核失败
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_EXTRACTION, operationDescribe = "商户提现审核失败")
    @RequestMapping(value = "noPassCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> failShopUserExtraction(@RequestBody IdBO bo) {
        ShopUserExtraction shopUserExtraction = shopUserExtractionService.getById(bo.getId());
        if (shopUserExtraction == null) {
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        ShopUserInfo shopUserInfo = shopUserInfoService.getById(shopUserExtraction.getShopUserId());
        if (shopUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        shopUserExtraction.setStatus(ExtractionStatusEnum.FAIL.getCode());
        boolean result = shopUserExtractionService.failShopUserExtraction(shopUserExtraction);
        if (!result) {
            log.error("商户审核失败{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }
}
