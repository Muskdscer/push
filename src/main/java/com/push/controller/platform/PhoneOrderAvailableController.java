package com.push.controller.platform;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.bo.platform.PhoneOrderAvailableBO;
import com.push.common.webfacade.vo.platform.PhoneOrderAvailableVO;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.service.platform.PhoneOrderAvailableService;
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
 * Description: 待处理订单列表controller
 * Create DateTime: 2020/3/31 9:29
 *
 *

 */
@RestController
@RequestMapping("order")
public class PhoneOrderAvailableController extends BaseController {

    @Resource
    private PhoneOrderAvailableService phoneOrderAvailableService;

    @RequestMapping(value = "/availableOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> availableOrder(@RequestBody PhoneOrderAvailableBO bo) {
        bo.validate();
        IPage<PhoneOrderAvailable> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        LambdaQueryWrapper<PhoneOrderAvailable> queryWrapper = new LambdaQueryWrapper<>(PhoneOrderAvailable.class);
        if (StringUtils.isNotBlank(bo.getPlatformOrderNo())) {
            queryWrapper.eq(PhoneOrderAvailable::getPlatformOrderNo, bo.getPlatformOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getUpstreamOrderNo())) {
            queryWrapper.eq(PhoneOrderAvailable::getUpstreamOrderNo, bo.getUpstreamOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getShopOrderNo())) {
            queryWrapper.eq(PhoneOrderAvailable::getShopOrderNo, bo.getShopOrderNo());
        }
        if (bo.getStartTime() != null) {
            queryWrapper.ge(PhoneOrderAvailable::getCreateTime, bo.getStartTime());
        }
        if (bo.getEndTime() != null) {
            queryWrapper.lt(PhoneOrderAvailable::getCreateTime, bo.getEndTime());
        }
        if (StringUtils.isNotBlank(bo.getPhoneOperator())) {
            queryWrapper.eq(PhoneOrderAvailable::getPhoneOperator, bo.getPhoneOperator());
        }
        if (bo.getPlatformOrderStatus() != null) {
            queryWrapper.eq(PhoneOrderAvailable::getPlatformOrderStatus, bo.getPlatformOrderStatus());
        }
        if (bo.getUpstreamCallbackStatus() != null) {
            queryWrapper.eq(PhoneOrderAvailable::getUpstreamCallbackStatus, bo.getUpstreamCallbackStatus());
        }
        if (bo.getShopCallbackStatus() != null) {
            queryWrapper.eq(PhoneOrderAvailable::getShopCallbackStatus, bo.getShopCallbackStatus());
        }
        if (StringUtils.isNotBlank(bo.getPhoneNum())) {
            queryWrapper.eq(PhoneOrderAvailable::getPhoneNum, bo.getPhoneNum());
        }
        if (StringUtils.isNotBlank(bo.getPushStatus())) {
            queryWrapper.eq(PhoneOrderAvailable::getPushStatus, bo.getPushStatus());
        }
        if (StringUtils.isNotBlank(bo.getShopName())){
            queryWrapper.eq(PhoneOrderAvailable::getShopName, bo.getShopName());
        }
        if (StringUtils.isNotBlank(bo.getUpstreamName())){
            queryWrapper.eq(PhoneOrderAvailable::getUpstreamName, bo.getUpstreamName());
        }
        queryWrapper.orderByDesc(PhoneOrderAvailable::getCreateTime);
        //订单状态为0的
        //queryWrapper.eq(PhoneOrderAvailable::getPlatformOrderStatus, OrderStatusCodeEnum.ORDER_UNUSED.getCode());
        IPage<PhoneOrderAvailable> recordIPage = phoneOrderAvailableService.page(page, queryWrapper);
        /*List<PhoneOrderAvailableVO> vos = recordIPage
                .getRecords()
                .stream()
                .map(p -> BeanUtils.copyPropertiesChaining(p, PhoneOrderAvailableVO::new))
                .collect(Collectors.toList());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("total", recordIPage.getTotal());
        returnMap.put("list", vos);*/
        return returnResultMap(ResultMapInfo.GETSUCCESS, recordIPage);
    }
}
