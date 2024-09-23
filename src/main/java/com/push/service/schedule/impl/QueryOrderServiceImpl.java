package com.push.service.schedule.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.enums.*;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.vo.out.RedisCallbackOrderStatusToUpstreamVO;
import com.push.common.webfacade.vo.platform.PhoneOrderWaitCheck;
import com.push.dao.mapper.platform.CpTpPhoneOrderCallBackMapper;
import com.push.dao.mapper.platform.PhoneOrderAvailableMapper;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.dao.mapper.platform.TpPhoneOrderCallBackMapper;
import com.push.entity.platform.*;
import com.push.entity.shop.ShopUserInfo;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.platform.*;
import com.push.service.schedule.QueryOrderService;
import com.push.service.shop.ShopUserInfoService;
import com.push.service.upstream.UpstreamCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * Create DateTime: 2020-04-02 15:23
 *
 * 

 */
@Slf4j
@Service
public class QueryOrderServiceImpl implements QueryOrderService {

    @Value("${handler.count}")
    private Long handlerCount;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private TpPhoneOrderCallBackService tpPhoneOrderCallBackService;

    @Resource
    private PhoneOrderAvailableService phoneOrderAvailableService;

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private UpstreamCallBackService upstreamCallBackService;

    @Resource
    private CpTpPhoneOrderCallBackMapper cpTpPhoneOrderCallBackMapper;

    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;

    @Resource
    private TpPhoneOrderCallBackMapper tpPhoneOrderCallBackMapper;

    @Resource
    private PhoneOrderAvailableMapper phoneOrderAvailableMapper;

    @Resource
    private PhoneOrderTimeOutService phoneOrderTimeOutService;

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private ShopMatchClassifyService shopMatchClassifyService;


    @Override
    @Transactional
    public void handlerWaitCheckOrderSuccess() {
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_WAIT_CHECK_SUCCESS);

        if (size == null || size == 0) {
            return;
        }

        if (size > handlerCount) {
            size = handlerCount;
        }

        //将redis中数据装进集合
        List<PhoneOrderWaitCheck> orderWaitChecks = new ArrayList<>(100);
        for (int i = 0; i < size; i++) {
            String result = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_WAIT_CHECK_SUCCESS);
            if (result == null) {
                continue;
            }
            PhoneOrderWaitCheck orderWaitCheck = JSONObject.parseObject(result, PhoneOrderWaitCheck.class);
            orderWaitChecks.add(orderWaitCheck);
        }
        List<String> collect1 = orderWaitChecks.stream().map(PhoneOrderWaitCheck::getPlatformOrderNo).collect(Collectors.toList());
        log.info("定时消费查询成功队列订单号:{}", collect1.toString());
        orderWaitChecks = orderWaitChecks.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(orderWaitChecks)) {
            //遍历出不为空的对象
            List<PhoneOrderWaitCheck> orderWaitCheckNotNulls = orderWaitChecks.stream().filter(Objects::nonNull).collect(Collectors.toList());
            List<TpPhoneOrderCallBack> tpList = new ArrayList<>();
            List<CpTpPhoneOrderCallBack> cpTpList = new ArrayList<>();

            List<String> tpPlatformOrderNos = tpPhoneOrderCallBackMapper.getBatch(orderWaitCheckNotNulls);
            List<PhoneOrderWaitCheck> orderWaitCheckShopHadCallbacks = orderWaitCheckNotNulls.stream()
                    .filter(orderWaitCheck -> tpPlatformOrderNos.contains(orderWaitCheck.getPlatformOrderNo())).collect(Collectors.toList());

            //删除极限情况下已经回调订单
            orderWaitCheckNotNulls.removeAll(orderWaitCheckShopHadCallbacks);

            log.info("定时消费查询成功队列筛选后的订单号:{}", orderWaitCheckNotNulls.toString());
            if (CollectionUtils.isEmpty(orderWaitCheckNotNulls)) {
                return;
            }

            //此时集合中为不是空且商户没有回调的订单
            List<PhoneOrderRecord> records = new ArrayList<>();
            orderWaitCheckNotNulls.forEach(orderWaitCheck -> {
                PhoneOrderRecord phoneOrderRecord = new PhoneOrderRecord();
                phoneOrderRecord.setQueryNum(1)
                        .setUpBackNum(1)
                        .setPlatformOrderNo(orderWaitCheck.getPlatformOrderNo())
                        .setOrderSn(orderWaitCheck.getOrderSn());
                records.add(phoneOrderRecord);
                TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
                tpPhoneOrderCallBack.setPlatformOrderNo(orderWaitCheck.getPlatformOrderNo());
                tpPhoneOrderCallBack.setShopOrderNo(orderWaitCheck.getShopOrderNo());
                tpPhoneOrderCallBack.setMessage(orderWaitCheck.getMessage());
                tpPhoneOrderCallBack.setCertificate(orderWaitCheck.getCertificate());
                tpPhoneOrderCallBack.setStatus(orderWaitCheck.getStatus());
                tpPhoneOrderCallBack.setOrderSn(orderWaitCheck.getOrderSn());
                tpList.add(tpPhoneOrderCallBack);
                CpTpPhoneOrderCallBack cpTpPhoneOrderCallBack = BeanUtils.copyPropertiesChaining(tpPhoneOrderCallBack, CpTpPhoneOrderCallBack::new);
                cpTpList.add(cpTpPhoneOrderCallBack);
            });
            //批量更新订单表
            try {
                //如果发生死锁
                phoneOrderRecordMapper.updateBatchByPlatformOrderNo(records);
            } catch (Exception e) {
                //发生死锁将数据重新放回redis
                orderWaitCheckNotNulls.forEach(orderWaitCheck -> {
                    redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_WAIT_CHECK_SUCCESS, JSON.toJSONString(orderWaitCheck));
                });
                log.error("查单定时批量更新订单发生死锁");
                throw e;
            }

            //批量插入tp表
            try {
                List<TpPhoneOrderCallBack> tpListShopOrderNoIsNull = tpList.stream().filter(tp -> tp.getShopOrderNo() == null).collect(Collectors.toList());
                tpList.removeAll(tpListShopOrderNoIsNull);
                if (CollectionUtils.isNotEmpty(tpList)) {
                    tpPhoneOrderCallBackMapper.saveBatch(tpList);
                }
                if (CollectionUtils.isNotEmpty(tpListShopOrderNoIsNull)) {
                    tpPhoneOrderCallBackMapper.saveBatchShopOrderNoIsNull(tpListShopOrderNoIsNull);
                }

            } catch (DuplicateKeyException e) {
                orderWaitCheckNotNulls.forEach(orderWaitCheck -> {
                    //若到此部商户回调，将数据重新放回redis
                    redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_WAIT_CHECK_SUCCESS, JSON.toJSONString(orderWaitCheck));
                });
                log.error("查单定时批量插入tp发生重复key异常");
            }

            //批量插入cpTp表
            List<CpTpPhoneOrderCallBack> cpTpListShopOrderNoIsNull = cpTpList.stream().filter(cpTp -> cpTp.getShopOrderNo() == null).collect(Collectors.toList());
            cpTpList.removeAll(cpTpListShopOrderNoIsNull);
            if (CollectionUtils.isNotEmpty(cpTpList)) {
                cpTpPhoneOrderCallBackMapper.saveBatch(cpTpList);
            }
            if (CollectionUtils.isNotEmpty(cpTpListShopOrderNoIsNull)) {
                cpTpPhoneOrderCallBackMapper.saveBatchShopOrderNoIsNull(cpTpListShopOrderNoIsNull);
            }
            //批量查询订单表，并回调渠道
            List<String> platformOrderNos = orderWaitCheckNotNulls.stream().map(PhoneOrderWaitCheck::getPlatformOrderNo).collect(Collectors.toList());
            List<PhoneOrderRecord> phoneOrderRecords = phoneOrderRecordMapper.getOrderBatchByOrderNo(platformOrderNos);
            phoneOrderRecords.forEach(orderRecord -> {
                //筛选订单
                PhoneOrderWaitCheck waitCheck = orderWaitCheckNotNulls.stream()
                        .filter(orderWaitCheck -> orderWaitCheck.getPlatformOrderNo().equals(orderRecord.getPlatformOrderNo())).findFirst().get();
                //回调渠道
                UpstreamUserInfo upstreamUserInfo = phoneOrderRecordService.getUpstreamUserInfoByPlatformOrderNo(orderRecord.getPlatformOrderNo());

                upstreamCallBackService.callbackUpstream(upstreamUserInfo, orderRecord,
                        waitCheck.getOrderSn(), waitCheck.getCertificate(), waitCheck.getStatus());
            });
        }
    }


    @Override
    @Transactional
    public void handlerWaitCheckOrderFailed() {
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_WAIT_CHECK_FAILED);

        if (size == null || size == 0) {
            return;
        }

        if (size > handlerCount) {
            size = handlerCount;
        }

        //查询失败的订单
        List<PhoneOrderWaitCheck> orderWaitChecks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String result = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_WAIT_CHECK_FAILED);
            PhoneOrderWaitCheck orderWaitCheck = JSONObject.parseObject(result, PhoneOrderWaitCheck.class);
            if (orderWaitCheck != null) {
                orderWaitChecks.add(orderWaitCheck);
            }
        }
        List<TpPhoneOrderCallBack> tpPhoneOrderCallBackList = new ArrayList<>();
        orderWaitChecks.forEach(orderWaitCheck -> {
            TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
            tpPhoneOrderCallBack.setPlatformOrderNo(orderWaitCheck.getPlatformOrderNo()).setShopOrderNo(orderWaitCheck.getShopOrderNo())
                    .setStatus(ShopCallbackOrderStatusEnum.TIME_OUT.getCode());
            tpPhoneOrderCallBackList.add(tpPhoneOrderCallBack);
        });
        //批量插入tp表做记录
        tpPhoneOrderCallBackService.saveBatch(tpPhoneOrderCallBackList);

        //筛选出订单号
        List<String> platformOrderNos = orderWaitChecks.stream().map(PhoneOrderWaitCheck::getPlatformOrderNo).collect(Collectors.toList());
        //根据订单号批量查询订单
        List<PhoneOrderRecord> records = phoneOrderRecordMapper.getBatchByPlatformOrderNo(platformOrderNos);
        List<PhoneOrderUnavailable> unavailableRecords = new ArrayList<>();
        records.forEach(record -> {
            PhoneOrderUnavailable orderUnavailable = BeanUtils.copyPropertiesChaining(record, PhoneOrderUnavailable::new);
            orderUnavailable.setPlatformOrderStatus(OrderStatusCodeEnum.ORDER_TIME_OUT.getCode());
            orderUnavailable.setShopCallbackStatus(ShopCallbackOrderLogEnum.CHECK_TIME_OUT.getCode());
            orderUnavailable.setShopBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
            orderUnavailable.setUpstreamBillStatus(UpstreamCallbackBillStatusEnum.NOTBILL.getCode());
            orderUnavailable.setPlatformBillStatus(PlatformBillStatusEnum.NOTBILL.getCode());
            orderUnavailable.setQueryNum(1);
            orderUnavailable.setUpBackNum(1);
            orderUnavailable.setId(null);

            if (!record.getPushStatus().equals(PushStatusEnum.FAILED_PUSH.getValue())) {
                //批量增加不可用订单，筛选出推送失败的订单
                unavailableRecords.add(orderUnavailable);
            }

            record.setPlatformOrderStatus(OrderStatusCodeEnum.ORDER_TIME_OUT.getCode());
            record.setShopCallbackStatus(ShopCallbackOrderLogEnum.CHECK_TIME_OUT.getCode());
            record.setShopBillStatus(ShopCallbackBillStatusEnum.NOTBILL.getCode());
            record.setUpstreamBillStatus(UpstreamCallbackBillStatusEnum.NOTBILL.getCode());
            record.setPlatformBillStatus(PlatformBillStatusEnum.NOTBILL.getCode());
            record.setUpBackNum(1);
            record.setQueryNum(1);

        });
        //批量插入不可用表
//        phoneOrderUnavailableService.saveBatch(unavailableRecords);
        //批量更新总记录表
        phoneOrderRecordService.updateBatchById(records);
        records.forEach(orderRecord -> {
            PhoneOrderWaitCheck waitCheck = orderWaitChecks.stream()
                    .filter(orderWaitCheck -> orderWaitCheck.getPlatformOrderNo().equals(orderRecord.getPlatformOrderNo())).findFirst().get();
            //回调上游
            upCallbackFail(orderRecord, waitCheck);
        });


        //批量删除可用表
        phoneOrderAvailableMapper.deleteBatchByOrderNo(platformOrderNos);
    }

    private void upCallbackFail(PhoneOrderRecord phoneOrderRecord, PhoneOrderWaitCheck waitCheck) {
        UpstreamUserInfo upstreamUserInfo = phoneOrderRecordService.getUpstreamUserInfoByPlatformOrderNo(waitCheck.getPlatformOrderNo());
        RedisCallbackOrderStatusToUpstreamVO vo = new RedisCallbackOrderStatusToUpstreamVO();
        vo.setAppId(upstreamUserInfo.getAppId());
        TreeMap<String, String> data = new TreeMap<>();
        data.put("a", upstreamUserInfo.getAppKey());
        data.put("b", upstreamUserInfo.getAppId());
        data.put("c", phoneOrderRecord.getPlatformOrderNo());
        data.put("d", phoneOrderRecord.getPhoneNum());
        data.put("e", phoneOrderRecord.getOrderPrice().toPlainString());
        data.put("f", phoneOrderRecord.getPhoneOperator());
        data.put("g", upstreamUserInfo.getAppKey());
        String sign = SignUtils.generateSignature(data, SignTypeEnum.SHA256);
        vo.setSign(sign);
        vo.setPhoneOperator(phoneOrderRecord.getPhoneOperator());
        vo.setPhoneNum(phoneOrderRecord.getPhoneNum());
        vo.setStatus(ShopCallBackStatusConstant.FAIL);
        vo.setOrderNo(phoneOrderRecord.getUpstreamOrderNo());
        vo.setBizOrderNo(phoneOrderRecord.getPlatformOrderNo());
        vo.setOrderPrice(phoneOrderRecord.getOrderPrice());
        vo.setType(phoneOrderRecord.getType());
        vo.setCity(phoneOrderRecord.getCity());
        vo.setProvince(phoneOrderRecord.getProvince());
        vo.setOrderSn(phoneOrderRecord.getOrderSn());
        vo.setCertificate(waitCheck.getCertificate());

        Map<String, Object> map = new HashMap<>(2);
        map.put("data", JSON.toJSONString(vo));
        map.put("url", upstreamUserInfo.getPushSite());
        redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK, JSON.toJSONString(map));

        upstreamCallBackService.callbackUpstream(upstreamUserInfo, phoneOrderRecord,
                phoneOrderRecord.getOrderSn(), waitCheck.getCertificate(), ShopCallBackStatusConstant.FAIL);
        phoneOrderAvailableService.remove(Wrappers.lambdaQuery(PhoneOrderAvailable.class)
                .eq(PhoneOrderAvailable::getPlatformOrderNo, phoneOrderRecord.getPlatformOrderNo()));
    }

    @Override
    public void handlerWaitCheckOrderProcessing() {
        Long listSize = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_WAIT_CHECK_PROCESSING);

        if (listSize == null || listSize == 0) {
            return;
        }

        if (listSize > handlerCount) {
            listSize = handlerCount;
        }

        List<String> platformOrderNoList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            Object obj = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_WAIT_CHECK_PROCESSING);
            if (obj != null) {
                PhoneOrderWaitCheck phoneOrderWaitCheck = JSON.parseObject(obj.toString(), PhoneOrderWaitCheck.class);
                platformOrderNoList.add(phoneOrderWaitCheck.getPlatformOrderNo());
            }
        }

        List<PhoneOrderTimeOut> list = phoneOrderTimeOutService.list(Wrappers.lambdaQuery(PhoneOrderTimeOut.class)
                .in(PhoneOrderTimeOut::getPlatformOrderNo, platformOrderNoList));

        if (CollectionUtils.isNotEmpty(list)) {
            List<String> orderNoList = list.stream().map(PhoneOrderTimeOut::getPlatformOrderNo).collect(Collectors.toList());
            platformOrderNoList = platformOrderNoList.stream().filter(item -> !orderNoList.contains(item)).collect(Collectors.toList());
        }

        List<PhoneOrderTimeOut> phoneOrderTimeOutList = platformOrderNoList.stream().map(PhoneOrderTimeOut::new).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(phoneOrderTimeOutList)) {
            phoneOrderTimeOutService.saveBatch(phoneOrderTimeOutList);
        }
    }

    @Override
    public void checkOrderTimeOut() {
        int count = phoneOrderTimeOutService.count(Wrappers.lambdaQuery(PhoneOrderTimeOut.class)
                .eq(PhoneOrderTimeOut::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));

        if (count > 0) {
            phoneOrderTimeOutService.removeByOrderStatus();
        }
    }

    @Override
    public void waitCheckOrderException() {
        List<PhoneOrderTimeOut> list = phoneOrderTimeOutService.list(Wrappers.lambdaQuery(PhoneOrderTimeOut.class)
                .eq(PhoneOrderTimeOut::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));

        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<String> platformOrderNoList = list.stream().map(PhoneOrderTimeOut::getPlatformOrderNo).collect(Collectors.toList());

        platformOrderNoList.forEach(platformOrderNo -> {
            PhoneOrderRecord phoneOrderRecord = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                    .eq(PhoneOrderRecord::getPlatformOrderNo, platformOrderNo));
            ShopUserInfo shopUserInfo = shopUserInfoService.getById(phoneOrderRecord.getShopUserId());
            ShopMatchClassify matchClassify = shopMatchClassifyService.getById(shopUserInfo.getMatchClassifyId());
            PhoneOrderWaitCheck orderWaitCheck = new PhoneOrderWaitCheck();
            orderWaitCheck.setPlatformOrderNo(platformOrderNo);
            orderWaitCheck.setQuerySite(shopUserInfo.getQuerySite());
            orderWaitCheck.setClassifyName(matchClassify.getName());
            orderWaitCheck.setAppId(shopUserInfo.getAppId());
            orderWaitCheck.setAppKey(shopUserInfo.getAppKey());
            redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_WAIT_CHECK, JSON.toJSONString(orderWaitCheck));
        });
    }
}
