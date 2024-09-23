package com.push.controller.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.agent.UpstreamAgentBackExtractionBO;
import com.push.common.webfacade.bo.platform.AgentPassCheckExtractionBO;
import com.push.common.webfacade.bo.platform.UpstreamAgentExtractionBO;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.error.ErrorEnum;
import com.push.common.webfacade.vo.platform.AgentPassCheckExtractionVO;
import com.push.common.webfacade.vo.platform.AgentSuAndFaPassCheckExtractionVO;
import com.push.entity.agent.UpstreamAgentUserInfo;
import com.push.service.agent.UpstreamAgentExtractionService;
import com.push.service.agent.UpstreamAgentUserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description: 上游代理商提现Controller
 * Create DateTime: 2020/4/26 18:27
 *
 *

 */
@RestController
@RequestMapping("agentExtractionBack")
public class UpstreamAgentBackExtractionController extends BaseController {

    @Resource
    private UpstreamAgentExtractionService upstreamAgentExtractionService;

    @Resource
    private UpstreamAgentUserInfoService upstreamAgentUserInfoService;

    //提现申请
    @Operation(userType = UserTypeCodeEnum.UPSTREAM_AGENT_USER, operationType = OperationTypeEnum.UPSTREAM_AGENT_EXTRACTION, operationDescribe = "渠道代理商发起提现")
    @PostMapping("/agentExtraction")
    public Map<String,Object> agentExtraction(@RequestBody UpstreamAgentBackExtractionBO bo){
        bo.validate();
        Long userId = getUserId();
        UpstreamAgentUserInfo userInfo = upstreamAgentUserInfoService.getById(userId);
        //判断用户是否存在
        if (userInfo == null || DeleteFlagEnum.DELETE.getCode() == userInfo.getDeleteFlag()){
            throw new CommonException(ErrorEnum.UNKNOWN);
        }
        //判断余额是否足够
        if (userInfo.getBalance().subtract(userInfo.getFrozenMoney()).compareTo(bo.getExtractionMoney()) < 0) {
            return returnResultMap(ResultMapInfo.BALANCEDEFICIENCY);
        }
        UpstreamAgentExtractionBO upstreamAgentExtractionBO = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentExtractionBO::new);
        upstreamAgentExtractionBO.setUpstreamAgentUserId(userId);
        boolean result = upstreamAgentExtractionService.saveExtractionAndUpdateUser(userInfo,upstreamAgentExtractionBO);
        if (!result){
            return returnResultMap(ResultMapInfo.SUBMITFAIL);
        }
        return returnResultMap(ResultMapInfo.SUBMITSUCCESS);
    }


    //待审核列表
    @PostMapping("/passCheckList")
    public Map<String,Object> passCheckList(@RequestBody AgentPassCheckExtractionBO bo){
        bo.validate();
        Long userId = getUserId();
        IPage<AgentPassCheckExtractionVO> vos = upstreamAgentExtractionService.passCheckList(bo,ExtractionStatusEnum.TODO.getCode(),userId);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }
    //提现成功列表
    @PostMapping("/successPassCheckList")
    public Map<String,Object> successPassCheckList(@RequestBody AgentPassCheckExtractionBO bo){
        bo.validate();
        Long userId = getUserId();
        IPage<AgentSuAndFaPassCheckExtractionVO> vos = upstreamAgentExtractionService.passSuAndFaCheckList(bo,ExtractionStatusEnum.SUCCESSS.getCode(),userId);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }
    //提现失败列表
    @PostMapping("/failPassCheckList")
    public Map<String,Object> failPassCheckList(@RequestBody AgentPassCheckExtractionBO bo){
        bo.validate();
        Long userId = getUserId();
        IPage<AgentSuAndFaPassCheckExtractionVO> vos = upstreamAgentExtractionService.passSuAndFaCheckList(bo,ExtractionStatusEnum.FAIL.getCode(), userId);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }

}
