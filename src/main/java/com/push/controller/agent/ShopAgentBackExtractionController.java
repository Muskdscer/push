package com.push.controller.agent;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.TokenUtil;
import com.push.common.webfacade.bo.agent.ShopAgentBackExtractionBO;
import com.push.common.webfacade.bo.platform.ShopAgentExtractionRecordBO;
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
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/4/27 10:31
 *
 * 

 */
@RestController
@Slf4j
@RequestMapping("shopAgentExtractionBack")
public class ShopAgentBackExtractionController extends BaseController {

    @Resource
    private ShopAgentUserExtractionService shopAgentUserExtractionService;

    @Resource
    private ShopAgentUserInfoService shopAgentUserInfoService;

    //增加提现
    @Operation(userType = UserTypeCodeEnum.SHOP_AGENT_USER, operationType = OperationTypeEnum.SHOP_AGENT_EXTRACTION, operationDescribe = "商户代理商发起提现")
    @RequestMapping("extraction")
    public Map<String, Object> shopAgentExtraction(@RequestBody ShopAgentBackExtractionBO bo) {
        bo.validate();
        ShopAgentUserInfo shopAgentUserInfo = shopAgentUserInfoService.getById(getUserId());
        ShopAgentUserExtraction shopAgentUserExtraction = BeanUtils.copyPropertiesChaining(bo, ShopAgentUserExtraction::new);
        shopAgentUserExtraction.setId(null);
        shopAgentUserExtraction.setShopAgentUserId(getUserId()).setExtractionNumber(TokenUtil.generateOrderNo());
        //判断提现的金额是否不足
        if (shopAgentUserExtraction.getExtractionMoney().compareTo(shopAgentUserInfo.getBalance().subtract(shopAgentUserInfo.getFrozenMoney())) == 1) {
            //余额不足的情况
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        shopAgentUserExtraction.setUserMobile(shopAgentUserInfo.getPhoneNumber());
        //更新商户代理商冻结金额
        shopAgentUserInfo.setFrozenMoney(shopAgentUserInfo.getFrozenMoney().add(shopAgentUserExtraction.getExtractionMoney()));
        boolean save = shopAgentUserExtractionService.saveExtractionAndUpdateUserInfo(shopAgentUserInfo, shopAgentUserExtraction);
        if (!save) {
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }
        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }

    //待处理提现记录
    @RequestMapping(value = "waitCheck", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> shopAgentExtractionToDoList(@RequestBody ShopAgentExtractionRecordBO bo) {
        bo.setShopAgentUserId(getUserId());
        Page<ShopAgentCheckExtractionVO> page = shopAgentUserExtractionService.getShopAgentExtractionToDoList(ExtractionStatusEnum.TODO.getCode(), bo);
        return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }

    //提现成功记录
    @RequestMapping(value = "listExtractionSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopAgentExtractionSuccessList(@RequestBody ShopAgentExtractionRecordBO bo) {
        bo.setShopAgentUserId(getUserId());
        Page<ShopAgentCheckExtractionVO> page = shopAgentUserExtractionService.getShopAgentExtractionToDoList(ExtractionStatusEnum.SUCCESSS.getCode(), bo);
        return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }

    //提现失败记录
    @RequestMapping(value = "listExtractionFailed", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getShopAgentExtractionFailList(@RequestBody ShopAgentExtractionRecordBO bo) {
        bo.setShopAgentUserId(getUserId());
        Page<ShopAgentCheckExtractionVO> page = shopAgentUserExtractionService.getShopAgentExtractionToDoList(ExtractionStatusEnum.FAIL.getCode(), bo);
        return returnResultMap(ResultMapInfo.GETSUCCESS, page);
    }
}
