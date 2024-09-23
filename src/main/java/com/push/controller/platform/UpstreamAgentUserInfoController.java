package com.push.controller.platform;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.AddUpstreamAgentUserInfoBO;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.ModifyUpstreamAgentUserInfoBO;
import com.push.common.webfacade.bo.platform.UpstreamAgentUserPageBO;
import com.push.common.webfacade.vo.platform.UpstreamAgentUserInfoVO;
import com.push.entity.agent.UpstreamAgentUserInfo;
import com.push.service.agent.UpstreamAgentUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 上游代理商基础Controller
 * Create DateTime: 2020/4/26 13:55
 *
 * 

 */
@RestController
@RequestMapping("upstreamAgent")
public class UpstreamAgentUserInfoController extends BaseController {
    /*
     * addUpstreamAgent 增加
     * modifyUpstreamAgent 修改
     * removeUpstreamAgent 删除
     * detailUpstreamAgent 查询详情
     * listUpstream Agent 查询加分页
     * */
    @Resource
    private UpstreamAgentUserInfoService upstreamAgentUserInfoService;


    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO,operationDescribe = "平台增加渠道代理商")
    @PostMapping("/addUpstreamAgent")
    public Map<String, Object> addUpstream(@RequestBody AddUpstreamAgentUserInfoBO bo) {
        bo.validate();
        UpstreamAgentUserInfo userInfo = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentUserInfo::new);
        boolean save = upstreamAgentUserInfoService.save(userInfo);
        return save ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO,operationDescribe = "平台修改渠道代理商")
    @PostMapping("/modifyUpstreamAgent")
    public Map<String, Object> modifyUpstream(@RequestBody ModifyUpstreamAgentUserInfoBO bo) {
        bo.validate();
        UpstreamAgentUserInfo userInfo = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentUserInfo::new);
        userInfo.setUserName(null);
        boolean update = upstreamAgentUserInfoService.updateById(userInfo);
        return update ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO,operationDescribe = "平台删除渠道代理商")
    @PostMapping("/removeUpstreamAgent")
    public Map<String, Object> removeUpstream(@RequestBody IdBO bo) {
        bo.validate();
        UpstreamAgentUserInfo userInfo = upstreamAgentUserInfoService.getById(bo.getId());
        userInfo.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        boolean update = upstreamAgentUserInfoService.updateById(userInfo);
        return update ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @PostMapping("/detailUpstream")
    public Map<String, Object> detailUpstream(@RequestBody IdBO bo) {
        bo.validate();
        UpstreamAgentUserInfo userInfo = upstreamAgentUserInfoService.getById(bo.getId());
        if (userInfo == null || DeleteFlagEnum.DELETE.getCode() == userInfo.getDeleteFlag()) {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
        UpstreamAgentUserInfoVO upstreamAgentUserInfoVO = BeanUtils.copyPropertiesChaining(userInfo, UpstreamAgentUserInfoVO::new);
        return returnResultMap(ResultMapInfo.GETSUCCESS, upstreamAgentUserInfoVO);
    }

    @PostMapping("/listUpstreamAgent")
    public Map<String, Object> listUpstream(@RequestBody UpstreamAgentUserPageBO bo) {
        bo.validate();
        Page<UpstreamAgentUserInfo> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        LambdaQueryWrapper<UpstreamAgentUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bo.getUserName())) {
            queryWrapper.like(UpstreamAgentUserInfo::getUserName, bo.getUserName());
        }
        if (StringUtils.isNotBlank(bo.getUpstreamName())){
            queryWrapper.like(UpstreamAgentUserInfo::getUpstreamName, bo.getUpstreamName());
        }
        if (StringUtils.isNotBlank(bo.getPhoneNumber())){
            queryWrapper.like(UpstreamAgentUserInfo::getPhoneNumber, bo.getPhoneNumber());
        }
        if (bo.getStartTime() != null){
            queryWrapper.ge(UpstreamAgentUserInfo::getCreateTime,bo.getStartTime());
        }
        if (bo.getEndTime() != null){
            queryWrapper.lt(UpstreamAgentUserInfo::getCreateTime,bo.getEndTime());
        }
        queryWrapper.eq(UpstreamAgentUserInfo::getDeleteFlag,DeleteFlagEnum.NOT_DELETE.getCode());
        queryWrapper.orderByDesc(UpstreamAgentUserInfo::getCreateTime);
        Page<UpstreamAgentUserInfo> iPage = upstreamAgentUserInfoService.page(page, queryWrapper);
        List<UpstreamAgentUserInfoVO> list = iPage.getRecords().stream().map(u -> BeanUtils.copyPropertiesChaining(u, UpstreamAgentUserInfoVO::new)).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("records",list);
        map.put("total",iPage.getTotal());
        return returnResultMap(ResultMapInfo.GETSUCCESS, map);
    }
}
