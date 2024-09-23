package com.push.controller.platform;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.controller.BaseController;
import com.push.common.controller.ResultMapInfo;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.bo.platform.ChangeOrderFailedBO;
import com.push.common.webfacade.bo.platform.PhoneOrderRecordBO;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.service.platform.PhoneOrderRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Description: 订单列表controller
 * Create DateTime: 2020/3/31 9:26
 *
 *

 */
@RestController
@RequestMapping("order")
public class PhoneOrderRecordController extends BaseController {

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @RequestMapping(value = "/listOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> listOrder(@RequestBody PhoneOrderRecordBO bo) {
        bo.validate();
        IPage<PhoneOrderRecord> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        LambdaQueryWrapper<PhoneOrderRecord> queryWrapper = new LambdaQueryWrapper<>(PhoneOrderRecord.class);
        if (StringUtils.isNotBlank(bo.getPhoneNum())) {
            queryWrapper.eq(PhoneOrderRecord::getPhoneNum, bo.getPhoneNum());
        }
        if (StringUtils.isNotBlank(bo.getPlatformOrderNo())) {
            queryWrapper.eq(PhoneOrderRecord::getPlatformOrderNo, bo.getPlatformOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getUpstreamOrderNo())) {
            queryWrapper.eq(PhoneOrderRecord::getUpstreamOrderNo, bo.getUpstreamOrderNo());
        }
        if (StringUtils.isNotBlank(bo.getShopOrderNo())) {
            queryWrapper.eq(PhoneOrderRecord::getShopOrderNo, bo.getShopOrderNo());
        }
        if (bo.getStartTime() != null) {
            queryWrapper.ge(PhoneOrderRecord::getCreateTime, bo.getStartTime());
        }
        if (bo.getEndTime() != null) {
            queryWrapper.lt(PhoneOrderRecord::getCreateTime, bo.getEndTime());
        }
        if (StringUtils.isNotBlank(bo.getPhoneOperator())) {
            queryWrapper.eq(PhoneOrderRecord::getPhoneOperator, bo.getPhoneOperator());
        }
        if (bo.getPlatformOrderStatus() != null) {
            queryWrapper.eq(PhoneOrderRecord::getPlatformOrderStatus, bo.getPlatformOrderStatus());
        }
        if (bo.getUpstreamCallbackStatus() != null) {
            queryWrapper.eq(PhoneOrderRecord::getUpstreamCallbackStatus, bo.getUpstreamCallbackStatus());
        }
        if (bo.getShopCallbackStatus() != null) {
            queryWrapper.eq(PhoneOrderRecord::getShopCallbackStatus, bo.getShopCallbackStatus());
        }
        if (StringUtils.isNotBlank(bo.getPushStatus())) {
            queryWrapper.eq(PhoneOrderRecord::getPushStatus, bo.getPushStatus());
        }
        if (StringUtils.isNotBlank(bo.getShopName())){
            queryWrapper.eq(PhoneOrderRecord::getShopName, bo.getShopName());
        }
        if (StringUtils.isNotBlank(bo.getUpstreamName())){
            queryWrapper.eq(PhoneOrderRecord::getUpstreamName, bo.getUpstreamName());
        }
        queryWrapper.orderByDesc(PhoneOrderRecord::getCreateTime);
        IPage<PhoneOrderRecord> recordIPage = phoneOrderRecordService.page(page, queryWrapper);
        /*List<PhoneOrderRecordVO> vos = recordIPage
                .getRecords()
                .stream()
                .map(p -> BeanUtils.copyPropertiesChaining(p, PhoneOrderRecordVO::new))
                .collect(Collectors.toList());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("total", recordIPage.getTotal());
        returnMap.put("list", vos);*/
        return returnResultMap(ResultMapInfo.GETSUCCESS, recordIPage);
    }

    /**
     * 订单置失败
     *
     * @param bo ChangeOrderFailedBO
     * @return 处理结果
     */
    @RequestMapping("changeOrderFailed")
    public Map<String, Object> changeOrderFailed(@RequestBody ChangeOrderFailedBO bo) {
        bo.validate();

        phoneOrderRecordService.changeOrderFailed(bo.getPlatformOrderNoList());

        return returnResultMap(ResultMapInfo.ADDSUCCESS);
    }
}
