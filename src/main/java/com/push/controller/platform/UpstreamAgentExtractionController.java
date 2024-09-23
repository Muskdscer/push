package com.push.controller.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.*;
import com.push.common.webfacade.bo.platform.AgentPassCheckExtractionBO;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.UpstreamAgentExtractionBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.AgentPassCheckExtractionVO;
import com.push.common.webfacade.vo.platform.AgentSuAndFaPassCheckExtractionVO;
import com.push.entity.agent.UpstreamAgentExtraction;
import com.push.entity.agent.UpstreamAgentUserInfo;
import com.push.service.agent.UpstreamAgentExtractionService;
import com.push.service.agent.UpstreamAgentUserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Description: 上游代理商提现Controller
 * Create DateTime: 2020/4/26 18:27
 *
 * 

 */
@RestController
@RequestMapping("upstreamAgentExtraction")
public class UpstreamAgentExtractionController extends BaseController {

    @Resource
    private UpstreamAgentExtractionService upstreamAgentExtractionService;

    @Resource
    private UpstreamAgentUserInfoService upstreamAgentUserInfoService;

    //提现申请
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_AGENT_EXTRACTION,operationDescribe = "平台发起渠道代理商提现")
    @PostMapping("/agentExtraction")
    public Map<String,Object> agentExtraction(@RequestBody UpstreamAgentExtractionBO bo){
        bo.validate();
        if (bo.getExtractionMoney().compareTo(new BigDecimal(0)) <= 0) {
            return returnResultMap(ResultMapInfo.MINEXTRACTIONFAIL);
        }
        UpstreamAgentUserInfo userInfo = upstreamAgentUserInfoService.getById(bo.getUpstreamAgentUserId());
        //判断用户是否存在
        if (userInfo == null || DeleteFlagEnum.DELETE.getCode() == userInfo.getDeleteFlag()){
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        if (userInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        //判断余额是否足够
        if (userInfo.getBalance().subtract(userInfo.getFrozenMoney()).compareTo(bo.getExtractionMoney()) < 0) {
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        boolean result = upstreamAgentExtractionService.saveExtractionAndUpdateUser(userInfo,bo);
        if (!result){
            return returnResultMap(ResultMapInfo.SUBMITFAIL);
        }
        return returnResultMap(ResultMapInfo.SUBMITSUCCESS);
    }

    //审核成功
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_AGENT_EXTRACTION,operationDescribe = "渠道代理商提现审核成功")
    @PostMapping("/agentPassCheck")
    public Map<String,Object> agentPassCheck(@RequestBody IdBO bo){
        bo.validate();
        UpstreamAgentExtraction extraction = upstreamAgentExtractionService.getById(bo.getId());
        if (extraction == null){
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamAgentUserInfo upstreamAgentUserInfo = upstreamAgentUserInfoService.getById(extraction.getUpstreamAgentUserId());
        if (upstreamAgentUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        extraction.setStatus(ExtractionStatusEnum.SUCCESSS.getCode());
        boolean result = upstreamAgentExtractionService.updateByIdAndSaveBill(extraction);
        return result ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //审核失败
    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER, operationType = OperationTypeEnum.UPSTREAM_AGENT_EXTRACTION,operationDescribe = "渠道代理商提现审核失败")
    @PostMapping("/agentNotPassCheck")
    public Map<String,Object> agentNotPassCheck(@RequestBody IdBO bo){
        bo.validate();
        UpstreamAgentExtraction extraction = upstreamAgentExtractionService.getById(bo.getId());
        if (extraction == null){
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        UpstreamAgentUserInfo upstreamAgentUserInfo = upstreamAgentUserInfoService.getById(extraction.getUpstreamAgentUserId());
        if (upstreamAgentUserInfo.getStatus().equals(UserStatusEnum.DISABLE.getValue())) {
            return returnResultMap(ResultMapInfo.ACCOUNT_BAN);
        }
        extraction.setStatus(ExtractionStatusEnum.FAIL.getCode());
        boolean result = upstreamAgentExtractionService.updateByIdAndUpdateUser(extraction);
        return result ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    //待审核列表
    @PostMapping("/waitCheckList")
    public Map<String,Object> passCheckList(@RequestBody AgentPassCheckExtractionBO bo){
        bo.validate();
        IPage<AgentPassCheckExtractionVO> vos = upstreamAgentExtractionService.passCheckList(bo,ExtractionStatusEnum.TODO.getCode(), null);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }
    //提现成功列表
    @PostMapping("/successPassCheckList")
    public Map<String,Object> successPassCheckList(@RequestBody AgentPassCheckExtractionBO bo){
        bo.validate();
        IPage<AgentSuAndFaPassCheckExtractionVO> vos = upstreamAgentExtractionService.passSuAndFaCheckList(bo,ExtractionStatusEnum.SUCCESSS.getCode(), null);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }
    //提现失败列表
    @PostMapping("/failPassCheckList")
    public Map<String,Object> failPassCheckList(@RequestBody AgentPassCheckExtractionBO bo){
        bo.validate();
        IPage<AgentSuAndFaPassCheckExtractionVO> vos = upstreamAgentExtractionService.passSuAndFaCheckList(bo,ExtractionStatusEnum.FAIL.getCode(), null);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }

}
