package com.push.controller.upstream;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.bo.platform.PhoneOrderUnavailableBO;
import com.push.common.webfacade.vo.platform.PhoneOrderUnavailableVO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PhoneOrderUnavailable;
import com.push.service.platform.PhoneOrderUnavailableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/3/31 9:33
 *
 * 

 */
@RestController
@RequestMapping("back/upstreamOrder")
public class UpstreamBackOrderUnavailableController extends BaseController {

    @Resource
    private PhoneOrderUnavailableService phoneOrderUnavailableService;

    @RequestMapping(value = "/listUnavailableOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listUnavailableOrder(@RequestBody PhoneOrderUnavailableBO bo) {
        bo.validate();
        IPage<PhoneOrderUnavailable> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        LambdaQueryWrapper<PhoneOrderUnavailable> queryWrapper = new LambdaQueryWrapper<>(PhoneOrderUnavailable.class);
        queryWrapper.eq(PhoneOrderUnavailable::getUpstreamUserId, getUserId());
        if (StringUtils.isNotBlank(bo.getPlatformOrderNo())) {
            queryWrapper.eq(PhoneOrderUnavailable::getPlatformOrderNo, bo.getPlatformOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getUpstreamOrderNo())) {
            queryWrapper.eq(PhoneOrderUnavailable::getUpstreamOrderNo, bo.getUpstreamOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getShopOrderNo())) {
            queryWrapper.eq(PhoneOrderUnavailable::getShopOrderNo, bo.getShopOrderNo());
        }
        if (bo.getStartTime() != null) {
            queryWrapper.ge(PhoneOrderUnavailable::getCreateTime, bo.getStartTime());
        }
        if (bo.getEndTime() != null) {
            queryWrapper.lt(PhoneOrderUnavailable::getCreateTime, bo.getEndTime());
        }
        queryWrapper.orderByDesc(PhoneOrderUnavailable::getCreateTime);
        //订单状态为0的
        IPage<PhoneOrderUnavailable> recordIPage = phoneOrderUnavailableService.page(page, queryWrapper);
        List<PhoneOrderUnavailableVO> vos = recordIPage
                .getRecords()
                .stream()
                .map(p -> BeanUtils.copyPropertiesChaining(p, PhoneOrderUnavailableVO::new))
                .collect(Collectors.toList());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("total", recordIPage.getTotal());
        returnMap.put("list", vos);
        return returnResultMap(ResultMapInfo.GETSUCCESS, returnMap);
    }
}
