package com.push.controller.platform;

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
import com.push.common.webfacade.bo.platform.ShopAgentExtractionBO;
import com.push.common.webfacade.bo.platform.ShopAgentExtractionRecordBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.statistics.ShopAgentCheckExtractionVO;
import com.push.entity.agent.ShopAgentUserExtraction;
import com.push.entity.agent.ShopAgentUserInfo;
import com.push.service.agent.ShopAgentUserExtractionService;
import com.push.service.agent.ShopAgentUserInfoService;
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
 * Description:
 * Create DateTime: 2020/4/27 10:31
 *
 * 

 */
@RestController
@Slf4j
@RequestMapping("shopAgentExtraction")
public class ShopAgentExtractionController extends BaseController {

    @Resource
    private ShopAgentUserExtractionService shopAgentUserExtractionService;

    @Resource
    private ShopAgentUserInfoService shopAgentUserInfoService;

    //增加提现
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.SHOP_AGENT_EXTRACTION,operationDescribe = "平台发起商户代理商提现")
    @RequestMapping("extraction")
    public Map<String, Object> shopAgentExtraction(@RequestBody ShopAgentExtractionBO bo){
        bo.validate();
        if (bo.getExtractionMoney().compareTo(new BigDecimal(0)) <= 0) {
            return returnResultMap(ResultMapInfo.MINEXTRACTIONFAIL);
        }
        ShopAgentUserInfo shopAgentUserInfo = shopAgentUserInfoService.getById(bo.getId());
        if (shopAgentUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        ShopAgentUserExtraction shopAgentUserExtraction = BeanUtils.copyPropertiesChaining(bo, ShopAgentUserExtraction::new);
        shopAgentUserExtraction.setId(null);
        shopAgentUserExtraction.setShopAgentUserId(bo.getId()).setExtractionNumber(TokenUtil.generateOrderNo());
        //判断提现的金额是否不足
        if (shopAgentUserExtraction.getExtractionMoney().compareTo(shopAgentUserInfo.getBalance()
                .subtract(shopAgentUserInfo.getFrozenMoney())) > 0){
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        shopAgentUserExtraction.setUserMobile(bo.getUserMobile());
        //更新商户代理商冻结金额
        shopAgentUserInfo.setFrozenMoney(shopAgentUserInfo.getFrozenMoney().add(shopAgentUserExtraction.getExtractionMoney()));
        boolean save = shopAgentUserExtractionService.saveExtractionAndUpdateUserInfo(shopAgentUserInfo,shopAgentUserExtraction);
        if (!save){
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }
    //待处理提现记录
    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> shopAgentExtractionToDoList(@RequestBody ShopAgentExtractionRecordBO bo) {
        Page<ShopAgentCheckExtractionVO> page = shopAgentUserExtractionService.getShopAgentExtractionToDoList(ExtractionStatusEnum.TODO.getCode(), bo);
            return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }
    //提现成功记录
    @RequestMapping(value = "listExtractionSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopAgentExtractionSuccessList(@RequestBody ShopAgentExtractionRecordBO bo) {
        Page<ShopAgentCheckExtractionVO> page = shopAgentUserExtractionService.getShopAgentExtractionToDoList(ExtractionStatusEnum.SUCCESSS.getCode(), bo);
            return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }
    //提现失败记录
    @RequestMapping(value = "listExtractionFailed", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopAgentExtractionFailList(@RequestBody ShopAgentExtractionRecordBO bo) {
        Page<ShopAgentCheckExtractionVO> page = shopAgentUserExtractionService.getShopAgentExtractionToDoList(ExtractionStatusEnum.FAIL.getCode(), bo);
        return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }
    //审核成功
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_AGENT_EXTRACTION, operationDescribe = "商户代理商提现审核成功")
    @RequestMapping(value = "passCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> successShopAgentUserExtraction(@RequestBody IdBO bo) {
        ShopAgentUserExtraction shopAgentUserExtraction = shopAgentUserExtractionService.getById(bo.getId());
        if (null == shopAgentUserExtraction) {
            log.error("商户代理商审核成功接口异常，【出错时间】{}", DateUtil.dateToString(new Date(),"yyyy-MM-dd HH-mm-ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        ShopAgentUserInfo shopAgentUserInfo = shopAgentUserInfoService.getById(shopAgentUserExtraction.getShopAgentUserId());
        if (null == shopAgentUserInfo) {
            return returnResultMap(ResultMapInfo.SHOP_AGENT_NOT_EXIST);
        }
        if (shopAgentUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        //判断余额是否充足
        if (shopAgentUserInfo.getBalance().compareTo(shopAgentUserInfo.getFrozenMoney()) < 0) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        } else {
            //更改提现提现订单状态，增加账单事务表记录,增加账单表
            shopAgentUserExtraction.setStatus(ExtractionStatusEnum.SUCCESSS.getCode());
            shopAgentUserExtractionService.operatorSuccess(shopAgentUserExtraction, shopAgentUserInfo);
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
    }
    //审核失败
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.SHOP_AGENT_EXTRACTION, operationDescribe = "商户代理商提现审核失败")
    @RequestMapping(value = "noPassCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> failShopUserExtraction(@RequestBody IdBO bo) {
        ShopAgentUserExtraction shopAgentUserExtraction = shopAgentUserExtractionService.getById(bo.getId());
        if (shopAgentUserExtraction == null) {
            log.error("商户代理商审核失败接口异常，【出错时间】{}", DateUtil.dateToString(new Date(),"yyyy-MM-dd HH-mm-ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        ShopAgentUserInfo shopAgentUserInfo = shopAgentUserInfoService.getById(shopAgentUserExtraction.getShopAgentUserId());
        if (shopAgentUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        shopAgentUserExtraction.setStatus(ExtractionStatusEnum.FAIL.getCode());
        boolean result = shopAgentUserExtractionService.failShopAgentUserExtraction(shopAgentUserExtraction);
        if (!result) {
            log.error("商户代理商审核失败，【出错时间】{}",DateUtil.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }
}
