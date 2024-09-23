package com.push.service.schedule.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.OrderStatusCodeEnum;
import com.push.common.enums.UpstreamCallBackStatusEnum;
import com.push.common.enums.UpstreamCallbackBillStatusEnum;
import com.push.common.enums.UpstreamCallbackEnum;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.vo.out.RedisCallbackOrderStatusToUpstreamVO;
import com.push.common.webfacade.vo.out.UpstreamCallBackVO;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.dao.mapper.upstream.UpstreamCallBackMapper;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PhoneOrderUnavailable;
import com.push.entity.upstream.UpstreamCallBack;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.platform.PhoneOrderUnavailableService;
import com.push.service.platform.TpPhoneOrderCallBackService;
import com.push.service.schedule.UpstreamCallbackService;
import com.push.service.upstream.UpstreamUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020/4/2 17:34
 *
 *

 */
@Service
@Slf4j
public class UpstreamCallbackServiceImpl implements UpstreamCallbackService {

    @Value("${handler.count}")
    private Long handlerCount;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private PhoneOrderUnavailableService phoneOrderUnavailableService;

    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;

    @Resource
    private UpstreamCallbackService upstreamCallbackService;

    @Resource
    private UpstreamCallBackMapper upstreamCallBackMapper;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    @Resource
    private TpPhoneOrderCallBackService tpPhoneOrderCallBackService;

    @Override
    public void handleUpstreamCallbackSuccess() {
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_SUCCESS);
        if (size == null || size == 0) {
            return;
        }

        if (size > handlerCount) {
            size = handlerCount;
        }

        List<UpstreamCallBackVO> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String upstreamCallback = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_SUCCESS);
            UpstreamCallBackVO upstreamCallBackVO = JSONObject.parseObject(upstreamCallback, UpstreamCallBackVO.class);
            if (upstreamCallBackVO != null) {
                list.add(upstreamCallBackVO);
            }
        }

        if (CollectionUtils.isNotEmpty(list)) {
            List<String> orderNoList = list.stream().map(UpstreamCallBackVO::getBizOrderNo).collect(Collectors.toList());
            try {
                phoneOrderRecordMapper.updateByOrderNo(orderNoList);
                upstreamCallBackMapper.saveBatch(orderNoList,UpstreamCallbackEnum.SUCCESS.getCode());
            } catch (Exception e) {
                log.info("死锁订单:{}", orderNoList.toString());
                list.forEach(upstreamCallBackVO -> {
                    redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_SUCCESS, JSON.toJSONString(upstreamCallBackVO));
                });
                throw e;

            }
            log.info("【全部流程结束】:{},最后一个订单号:{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), orderNoList.get(orderNoList.size() - 1));
        }

    }

    @Override
    public void handleUpstreamCallbackFail() {
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_FAILED);
        if (size == null || size == 0) {
            return;
        }

        if (size > handlerCount) {
            size = handlerCount;
        }

        for (int i = 0; i < size; i++) {
            String result = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK_FAILED);
            Map map = JSONObject.parseObject(result, Map.class);
            Object data = map.get("data");
            RedisCallbackOrderStatusToUpstreamVO vo = JSONObject.parseObject(data.toString(), RedisCallbackOrderStatusToUpstreamVO.class);
            List<PhoneOrderRecord> phoneOrderRecords = phoneOrderRecordMapper
                    .selectList(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                            .eq(PhoneOrderRecord::getPlatformOrderNo, vo.getBizOrderNo())
                            .ne(PhoneOrderRecord::getUpstreamCallbackStatus, UpstreamCallbackEnum.SUCCESS.getCode()));
            phoneOrderRecords.forEach(phoneOrderRecord -> {
                if (phoneOrderRecord.getUpBackNum() != 3) {
                    upstreamCallbackService.repeatHandleSuccess(phoneOrderRecord, result);
                } else {
                    upstreamCallbackService.repeatHandleFail(phoneOrderRecord);
                }
                UpstreamCallBack upstreamCallBack = new UpstreamCallBack();
                upstreamCallBack.setPlatformOrderNo(phoneOrderRecord.getPlatformOrderNo()).setStatus(0);
                upstreamCallBackMapper.insert(upstreamCallBack);
            });
        }
    }

    @Override
    @TransactionalWithRollback
    public void repeatHandleFail(PhoneOrderRecord phoneOrderRecord) {
        if (phoneOrderRecord.getUpstreamCallbackStatus() == UpstreamCallbackEnum.FAIL.getCode()) {
            return;
        }
        PhoneOrderRecord record = new PhoneOrderRecord();
        if (!phoneOrderRecord.getPlatformOrderStatus().equals(OrderStatusCodeEnum.ORDER_TIME_OUT.getCode())) {
            //超时订单不用更改订单状态
            record.setPlatformOrderStatus(OrderStatusCodeEnum.UPSTREAM_RESPONSE_FAIL.getCode());
        }
        record.setId(phoneOrderRecord.getId());
        record.setUpstreamCallbackStatus(UpstreamCallbackEnum.FAIL.getCode());
        phoneOrderRecordService.update(record, Wrappers.lambdaUpdate(PhoneOrderRecord.class)
                //.eq(PhoneOrderRecord::getUpstreamCallbackStatus, UpstreamCallbackEnum.FAIL.getCode())
                .eq(PhoneOrderRecord::getId,record.getId()));
        //判断不可用表中是否有该订单，如果有则更新，如果没有则保存
        /*PhoneOrderUnavailable phoneOrderUnavailable = phoneOrderUnavailableService.getOne(Wrappers.lambdaQuery(PhoneOrderUnavailable.class)
                .eq(PhoneOrderUnavailable::getPlatformOrderNo, phoneOrderRecord.getPlatformOrderNo()));
        PhoneOrderUnavailable result = new PhoneOrderUnavailable();
        if (phoneOrderUnavailable == null) {
            phoneOrderRecord.setUpstreamBillStatus(UpstreamCallbackBillStatusEnum.NOTBILL.getCode());
            if (!phoneOrderRecord.getPlatformOrderStatus().equals(OrderStatusCodeEnum.ORDER_TIME_OUT.getCode())) {
                //超时订单不用更改订单状态
                phoneOrderRecord.setPlatformOrderStatus(OrderStatusCodeEnum.UPSTREAM_RESPONSE_FAIL.getCode());
            }
            phoneOrderRecord.setUpstreamCallbackStatus(UpstreamCallbackEnum.FAIL.getCode());
            phoneOrderRecord.setId(null);
            BeanUtils.copyProperties(phoneOrderRecord, result);
            phoneOrderUnavailableService.save(result);
        } else {
            phoneOrderUnavailable.setUpstreamBillStatus(UpstreamCallbackBillStatusEnum.NOTBILL.getCode())
                    .setUpstreamCallbackStatus(UpstreamCallbackEnum.FAIL.getCode());
            phoneOrderUnavailableService.update(phoneOrderUnavailable, Wrappers.lambdaUpdate(PhoneOrderUnavailable.class)
                    //.eq(PhoneOrderUnavailable::getUpstreamCallbackStatus, UpstreamCallbackEnum.FAIL.getCode())
                    .eq(PhoneOrderUnavailable::getId,phoneOrderUnavailable.getId()));
        }*/
    }

    @Override
    public void repeatHandleSuccess(PhoneOrderRecord phoneOrderRecord, String result) {
        PhoneOrderRecord record = new PhoneOrderRecord();
        record.setUpBackNum(phoneOrderRecord.getUpBackNum() + 1);
        record.setId(phoneOrderRecord.getId());
        record.setUpstreamCallbackStatus(UpstreamCallbackEnum.FAIL.getCode());
        phoneOrderRecordService.updateById(record);

        redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK, result);
    }

}
