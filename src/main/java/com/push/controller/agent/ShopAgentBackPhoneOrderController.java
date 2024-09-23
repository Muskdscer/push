package com.push.controller.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.webfacade.bo.agent.AgentPhoneOrderRecordBO;
import com.push.common.webfacade.vo.platform.PhoneOrderRecordVO;
import com.push.entity.agent.ShopAgentMapInfo;
import com.push.service.agent.ShopAgentMapInfoService;
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
@RequestMapping("shopAgentBackPhoneOrder")
public class ShopAgentBackPhoneOrderController extends BaseController {

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private ShopAgentMapInfoService shopAgentMapInfoService;

    @PostMapping("shopAgentOrderList")
    public Map<String,Object> shopAgentOrderList(@RequestBody AgentPhoneOrderRecordBO bo){
        Long userId = getUserId();
        List<ShopAgentMapInfo> list = shopAgentMapInfoService.list(Wrappers.lambdaQuery(ShopAgentMapInfo.class)
                .eq(ShopAgentMapInfo::getAgentId, userId)
                .eq(ShopAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode())
                .orderByDesc(ShopAgentMapInfo::getCreateTime));
        if (CollectionUtils.isEmpty(list)){
            return returnResultMap(ResultMapInfo.GETSUCCESS,list);
        }
        List<Long> ids = list.stream().map(ShopAgentMapInfo::getShopId).collect(Collectors.toList());
        IPage<PhoneOrderRecordVO> vos = phoneOrderRecordService.listByShopIds(bo,ids);
        return returnResultMap(ResultMapInfo.GETSUCCESS,vos);
    }

}
