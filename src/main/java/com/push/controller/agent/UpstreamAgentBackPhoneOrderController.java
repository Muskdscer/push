package com.push.controller.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.webfacade.bo.agent.AgentPhoneOrderRecordBO;
import com.push.common.webfacade.vo.platform.PhoneOrderRecordVO;
import com.push.entity.agent.UpstreamAgentMapInfo;
import com.push.service.agent.UpstreamAgentMapInfoService;
import com.push.service.platform.PhoneOrderRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/4/28 16:05
 *
 *

 */
@RestController
@RequestMapping("UpstreamAgentBackPhoneOrder")
public class UpstreamAgentBackPhoneOrderController extends BaseController {

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private UpstreamAgentMapInfoService UpstreamAgentMapInfoService;

    @PostMapping("UpstreamAgentOrderList")
    public Map<String,Object> UpstreamAgentOrderList(@RequestBody AgentPhoneOrderRecordBO bo){
        Long userId = getUserId();
        List<UpstreamAgentMapInfo> list = UpstreamAgentMapInfoService.list(Wrappers.lambdaQuery(UpstreamAgentMapInfo.class)
                .eq(UpstreamAgentMapInfo::getAgentId, userId)
                .eq(UpstreamAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode())
                .orderByDesc(UpstreamAgentMapInfo::getCreateTime));
        if (CollectionUtils.isEmpty(list)){
            return returnResultMap(ResultMapInfo.GETSUCCESS,list);
        }
        List<Long> ids = list.stream().map(UpstreamAgentMapInfo::getUpstreamId).collect(Collectors.toList());
        IPage<PhoneOrderRecordVO> vos = phoneOrderRecordService.listByUpstreamIds(bo,ids);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }

}
