package com.push.service.schedule.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.enums.OrderStatusCodeEnum;
import com.push.common.enums.PushStatusEnum;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.SjShopOrderNoBO;
import com.push.common.webfacade.vo.out.RedisCallbackOrderStatusToUpstreamVO;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderFailRecord;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PhoneOrderUnavailable;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.platform.PhoneOrderAvailableService;
import com.push.service.platform.PhoneOrderFailRecordService;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.platform.PhoneOrderUnavailableService;
import com.push.service.schedule.OrderService;
import com.push.service.upstream.UpstreamCallBackService;
import com.push.service.upstream.UpstreamUserInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description:
 * Create DateTime: 2020/4/26 18:01
 *
 * 

 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private PhoneOrderFailRecordService phoneOrderFailRecordService;

    @Resource
    private PhoneOrderAvailableService phoneOrderAvailableService;

    @Resource
    private UpstreamCallBackService upstreamCallBackService;

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    @Override
    @TransactionalWithRollback
    public void handlerFailedOrder(PhoneOrderAvailable phoneOrderAvailable, Boolean isException) {
        phoneOrderFailRecordService.save(BeanUtils.copyPropertiesChaining(phoneOrderAvailable, PhoneOrderFailRecord::new));

        if (isException) {
            return;
        }

        PhoneOrderRecord phoneOrderRecord = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, phoneOrderAvailable.getPlatformOrderNo()));

        //加入回调渠道队列
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(phoneOrderAvailable.getUpstreamUserId());

        upstreamCallBackService.callbackUpstream(upstreamUserInfo, phoneOrderRecord, "", "", ShopCallBackStatusConstant.FAIL);

        //从可用表移除
        phoneOrderAvailableService.remove(Wrappers.lambdaUpdate(PhoneOrderAvailable.class)
                .eq(PhoneOrderAvailable::getPlatformOrderNo, phoneOrderAvailable.getPlatformOrderNo()));

        //更新总记录表
        phoneOrderRecordService.update(Wrappers.lambdaUpdate(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, phoneOrderAvailable.getPlatformOrderNo())
                .set(PhoneOrderRecord::getPlatformOrderStatus, OrderStatusCodeEnum.PUSH_FAILED.getCode())
                .set(PhoneOrderRecord::getPushStatus, PushStatusEnum.FAILED_PUSH.getValue())
                .set(PhoneOrderRecord::getUpBackNum, 1));

        //插入不可用表
        /*PhoneOrderUnavailable phoneOrderUnavailable = BeanUtils.copyPropertiesChaining(phoneOrderAvailable, PhoneOrderUnavailable::new);
        phoneOrderUnavailable.setPushStatus(PushStatusEnum.FAILED_PUSH.getValue());
        phoneOrderUnavailable.setPlatformOrderStatus(OrderStatusCodeEnum.PUSH_FAILED.getCode());
        phoneOrderUnavailableService.save(phoneOrderUnavailable);*/
    }

    @Override
    public void handlerPushOrderSuccessForSJ(List<SjShopOrderNoBO> sjShopOrderNoBOList) {
        List<PhoneOrderRecord> phoneOrderRecordList = new ArrayList<>();
        sjShopOrderNoBOList.forEach(item -> {
            PhoneOrderRecord phoneOrderRecord = new PhoneOrderRecord();
            phoneOrderRecord.setPlatformOrderNo(item.getPlatformOrderNo());
            phoneOrderRecord.setShopOrderNo(item.getShopOrderNo());

            phoneOrderRecordList.add(phoneOrderRecord);
        });

        phoneOrderRecordService.batchUpdateShopOrderNoByPlatformOrderNo(phoneOrderRecordList);
    }
}
