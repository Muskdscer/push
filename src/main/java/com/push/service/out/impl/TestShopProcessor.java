package com.push.service.out.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.TestCallbackOrderStatusBO;
import com.push.common.webfacade.vo.out.RedisCallbackOrderStatusToUpstreamVO;
import com.push.dao.mapper.platform.CpTpPhoneOrderCallBackMapper;
import com.push.entity.platform.CpTpPhoneOrderCallBack;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.TpPhoneOrderCallBack;
import com.push.entity.platform.TpPhoneOrderCallBackLog;
import com.push.entity.shop.ShopUserInfo;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.out.ShopProcessor;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.platform.TpPhoneOrderCallBackLogService;
import com.push.service.platform.TpPhoneOrderCallBackService;
import com.push.service.shop.ShopUserInfoService;
import com.push.service.upstream.UpstreamCallBackService;
import com.push.service.upstream.UpstreamUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Create DateTime: 2020/4/21 10:58
 *
 *

 */
@Slf4j
@Service
public class TestShopProcessor implements ShopProcessor {

    @Resource
    private TpPhoneOrderCallBackLogService tpPhoneOrderCallBackLogService;

    @Resource
    private TpPhoneOrderCallBackService tpPhoneOrderCallBackService;

    @Resource
    private UpstreamUserInfoService upstreamUserInfoService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private CpTpPhoneOrderCallBackMapper cpTpPhoneOrderCallBackMapper;

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private UpstreamCallBackService upstreamCallBackService;

    @Override
    public ShopUserInfo getShopUserInfo(String param) {
        TestCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, TestCallbackOrderStatusBO.class);
        return shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class).eq(ShopUserInfo::getAppId, callbackOrderStatusBO.getAppId()));
    }

    @Override
    public boolean validateParam(String param) {
        TestCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, TestCallbackOrderStatusBO.class);
        return !StringUtils.isBlank(callbackOrderStatusBO.getUserOrderNumber())
                && !StringUtils.isBlank(callbackOrderStatusBO.getDemandNumber())
                && callbackOrderStatusBO.getStatus() != null;
    }

    @Override
    public PhoneOrderRecord getPhoneOrderRecord(String param) {
        TestCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, TestCallbackOrderStatusBO.class);
        return phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, callbackOrderStatusBO.getUserOrderNumber()));
    }

    @Override
    public boolean validateSign(String param, String appKey) {
        TestCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, TestCallbackOrderStatusBO.class);
        String sb = appKey + callbackOrderStatusBO.getAppId() +
                callbackOrderStatusBO.getDemandNumber() +
                callbackOrderStatusBO.getUserOrderNumber() +
                callbackOrderStatusBO.getOrderSn() +
                callbackOrderStatusBO.getCertificate() +
                callbackOrderStatusBO.getStatus().toString() +
                appKey;
        String sign = SecureUtil.sha256(sb);
        return StringUtils.equals(sign, callbackOrderStatusBO.getSign());
    }

    @Override
    public void postProcess(String param, PhoneOrderRecord phoneOrderRecord) {
        TestCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, TestCallbackOrderStatusBO.class);

        TpPhoneOrderCallBack orderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, callbackOrderStatusBO.getUserOrderNumber()));

        if (orderCallBack != null) {
            return;
        }

        TpPhoneOrderCallBack tpPhoneOrderCallBack1 = createTpPhoneOrderCallBack(callbackOrderStatusBO);
        tpPhoneOrderCallBackService.saveOrUpdateByPlatformOrderNo(tpPhoneOrderCallBack1);
        if (phoneOrderRecord.getQueryNum() == 0) {
            CpTpPhoneOrderCallBack cpTpPhoneOrderCallBack = BeanUtils.copyPropertiesChaining(tpPhoneOrderCallBack1, CpTpPhoneOrderCallBack::new);
            cpTpPhoneOrderCallBackMapper.insert(cpTpPhoneOrderCallBack);
        }

        //向Redis中发送数据
        UpstreamUserInfo upstreamUserInfo = upstreamUserInfoService.getById(phoneOrderRecord.getUpstreamUserId());

        upstreamCallBackService.callbackUpstream(upstreamUserInfo, phoneOrderRecord,
                tpPhoneOrderCallBack1.getOrderSn(), tpPhoneOrderCallBack1.getCertificate(), tpPhoneOrderCallBack1.getStatus());
        redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_SHOP_CALL_BACK_STATUS + phoneOrderRecord.getPlatformOrderNo(), "1", 30L, TimeUnit.MINUTES);
    }

    @Override
    public void createShopCallbackOrderLog(String param, Long shopUserId, Integer status, String remark) {
        TestCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, TestCallbackOrderStatusBO.class);
        TpPhoneOrderCallBackLog orderLog = new TpPhoneOrderCallBackLog();
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setPlatformOrderNo(callbackOrderStatusBO.getUserOrderNumber())
                .setShopUserId(shopUserId)
                .setStatus(status)
                .setAppId(callbackOrderStatusBO.getAppId());
        tpPhoneOrderCallBackLogService.save(orderLog);
    }

    @Override
    public String responseSuccessResult() {
        return ShopCallBackStatusConstant.SUCCESSFUL;
    }

    @Override
    public String responseFailedResult() {
        return ShopCallBackStatusConstant.FAILED;
    }

    private TpPhoneOrderCallBack createTpPhoneOrderCallBack(TestCallbackOrderStatusBO bo) {
        TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
        tpPhoneOrderCallBack.setStatus(bo.getStatus().equals(1) ? 1:0);
        tpPhoneOrderCallBack.setMessage(bo.getDetailMessage());
        tpPhoneOrderCallBack.setPlatformOrderNo(bo.getUserOrderNumber());
        tpPhoneOrderCallBack.setShopOrderNo(bo.getDemandNumber());
        tpPhoneOrderCallBack.setMessage(bo.getDetailMessage());
        tpPhoneOrderCallBack.setOrderSn(bo.getOrderSn());
        tpPhoneOrderCallBack.setCertificate(bo.getCertificate());
        return tpPhoneOrderCallBack;
    }
}
