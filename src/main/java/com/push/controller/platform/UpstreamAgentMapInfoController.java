package com.push.controller.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.annotation.Operation;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.bo.platform.AddUpstreamAgentMapInfoBO;
import com.push.common.webfacade.bo.platform.IdBO;
import com.push.common.webfacade.bo.platform.ModifyUpstreamAgentMapInfoBO;
import com.push.common.webfacade.vo.platform.IdPageBO;
import com.push.common.webfacade.vo.platform.UpstreamAgentMapInfoVO;
import com.push.entity.agent.UpstreamAgentMapInfo;
import com.push.service.agent.UpstreamAgentMapInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description: 上游代理商代理Controller
 * Create DateTime: 2020/4/26 16:48
 *
 * 

 */
@RestController
@RequestMapping("agentMap")
public class UpstreamAgentMapInfoController extends BaseController {
    /*
     * addUpstreamAgentMap 增加
     * modifyUpstreamAgentMap 修改
     * removeUpstreamAgentMap 删除
     * listUpstreamAgentMap 查询加分页
     * */
    @Resource
    private UpstreamAgentMapInfoService upstreamAgentMapInfoService;

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO,operationDescribe = "平台增加渠道代理商与渠道绑定关系")
    @PostMapping("/addUpstreamAgentMap")
    public Map<String, Object> addUpstreamAgentMap(@RequestBody AddUpstreamAgentMapInfoBO bo) {
        bo.validate();
        UpstreamAgentMapInfo upstreamAgentMapInfo = upstreamAgentMapInfoService.getOne(Wrappers.lambdaQuery(UpstreamAgentMapInfo.class)
                .eq(UpstreamAgentMapInfo::getUpstreamId, bo.getUpstreamId())
                .eq(UpstreamAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
        if (upstreamAgentMapInfo != null){
            return returnResultMap(ResultMapInfo.UPSTREAM_AGENT_EXIST);
        }
        UpstreamAgentMapInfo mapInfo = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentMapInfo::new);
        boolean save = upstreamAgentMapInfoService.save(mapInfo);
        return save ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO,operationDescribe = "平台修改渠道代理商与渠道绑定关系")
    @PostMapping("/modifyUpstreamAgentMap")
    public Map<String, Object> modifyUpstreamAgentMap(@RequestBody ModifyUpstreamAgentMapInfoBO bo) {
        bo.validate();
        UpstreamAgentMapInfo mapInfo = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentMapInfo::new);
        boolean update = upstreamAgentMapInfoService.updateById(mapInfo);
        return update ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @Operation(userType = UserTypeCodeEnum.PLATFORM_USER,operationType = OperationTypeEnum.UPSTREAM_AGENT_USER_INFO,operationDescribe = "平台删除渠道代理商与渠道绑定关系")
    @PostMapping("/removeUpstreamAgentMap")
    public Map<String, Object> removeUpstreamAgentMap(@RequestBody IdBO bo) {
        bo.validate();
        UpstreamAgentMapInfo mapInfo = BeanUtils.copyPropertiesChaining(bo, UpstreamAgentMapInfo::new);
        mapInfo.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
        boolean remove = upstreamAgentMapInfoService.updateById(mapInfo);
        return remove ? returnResultMap(ResultMapInfo.ADDSUCCESS) : returnResultMap(ResultMapInfo.ADDFAIL);
    }

    @PostMapping("/listUpstreamAgentMap")
    public Map<String, Object> listUpstreamAgentMap(@RequestBody IdPageBO bo) {
        bo.validate();
        Page<UpstreamAgentMapInfo> page = new Page<>(bo.getPageNo(),bo.getPageSize());
        IPage<UpstreamAgentMapInfoVO> iPage = upstreamAgentMapInfoService.pageWithUpstream(page,bo.getAgentId(),DeleteFlagEnum.NOT_DELETE.getCode());
        return returnResultMap(ResultMapInfo.GETSUCCESS,iPage);
    }
}
