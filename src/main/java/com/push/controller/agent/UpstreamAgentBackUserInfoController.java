package com.push.controller.agent;

import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.agent.ModifyUpstreamAgentBackUserInfoBO;
import com.push.common.webfacade.bo.shop.ModifyShopBackPasswordBO;
import com.push.common.webfacade.vo.platform.UpstreamAgentUserInfoVO;
import com.push.entity.agent.UpstreamAgentUserInfo;
import com.push.service.agent.UpstreamAgentUserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description: 上游代理商基础Controller
 * Create DateTime: 2020/4/26 13:55
 *
 * 

 */
@RestController
@RequestMapping("upstreamAgentBack")
public class UpstreamAgentBackUserInfoController extends BaseController {
    /*
     * modifyUpstreamAgent 修改
     * detailUpstreamAgent 查询详情
     * */
    @Resource
    private UpstreamAgentUserInfoService upstreamAgentUserInfoService;


    @Operation(userType = UserTypeCodeEnum.UPSTREAM_AGENT_USER, operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO, operationDescribe = "渠道代理商修改信息")
    @PostMapping("/modifyUpstreamAgent")
    public Map<String, Object> modifyUpstream(@RequestBody ModifyUpstreamAgentBackUserInfoBO bo) {
        bo.validate();
        Long userId = getUserId();
        UpstreamAgentUserInfo userInfo = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentUserInfo::new);
        userInfo.setId(userId);
        boolean update = upstreamAgentUserInfoService.updateById(userInfo);
        return update ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }


    @PostMapping("/detailUpstream")
    public Map<String, Object> detailUpstream() {
        Long userId = getUserId();
        UpstreamAgentUserInfo userInfo = upstreamAgentUserInfoService.getById(userId);
        if (userInfo == null || DeleteFlagEnum.DELETE.getCode() == userInfo.getDeleteFlag()) {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
        UpstreamAgentUserInfoVO upstreamAgentUserInfoVO = BeanUtils.copyPropertiesChaining(userInfo, UpstreamAgentUserInfoVO::new);
        return returnResultMap(ResultMapInfo.GETSUCCESS, upstreamAgentUserInfoVO);
    }

    @Operation(userType = UserTypeCodeEnum.UPSTREAM_AGENT_USER, operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO, operationDescribe = "渠道代理商修改密码")
    @PostMapping("modifyPwd")
    public Map<String, Object> modifyPwd(@RequestBody ModifyShopBackPasswordBO bo) {
        UpstreamAgentUserInfo userInfo = new UpstreamAgentUserInfo();
        userInfo.setId(getUserId());
        userInfo.setPassword(bo.getNewPwd());
        boolean update = upstreamAgentUserInfoService.updateById(userInfo);
        if (update) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        }
        return returnResultMap(ResultMapInfo.ADDFAIL);
    }
}
