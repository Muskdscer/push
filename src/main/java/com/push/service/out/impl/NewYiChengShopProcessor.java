package com.push.service.out.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.HappyCallbackOrderStatusBO;
import com.push.common.webfacade.bo.out.NewYiChengCallbackOrderStatusBO;
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
 * Create DateTime: 2021-03-19 14:20
 *
 *

 */
@Slf4j
@Service
public class NewYiChengShopProcessor implements ShopProcessor {

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
        NewYiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, NewYiChengCallbackOrderStatusBO.class);

        PhoneOrderRecord phoneOrderRecord = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, callbackOrderStatusBO.getUser_order_id()));
        return shopUserInfoService.getById(phoneOrderRecord.getShopUserId());
    }

    @Override
    public boolean validateParam(String param) {
        NewYiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, NewYiChengCallbackOrderStatusBO.class);
        return StringUtils.isNotBlank(callbackOrderStatusBO.getSys_order_id())
                && StringUtils.isNotBlank(callbackOrderStatusBO.getUser_order_id())
                && StringUtils.isNotBlank(callbackOrderStatusBO.getSign())
                && callbackOrderStatusBO.getStatus() != null;
    }

    @Override
    public PhoneOrderRecord getPhoneOrderRecord(String param) {
        NewYiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, NewYiChengCallbackOrderStatusBO.class);
        return phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, callbackOrderStatusBO.getUser_order_id()));
    }

    @Override
    public boolean validateSign(String param, String appKey) {
        NewYiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, NewYiChengCallbackOrderStatusBO.class);

        PhoneOrderRecord phoneOrderRecord = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, callbackOrderStatusBO.getUser_order_id()));
        ShopUserInfo userInfo = shopUserInfoService.getById(phoneOrderRecord.getShopUserId());

        String sb = "kaijin001"
                + callbackOrderStatusBO.getSys_order_id() + callbackOrderStatusBO.getUser_order_id()
                + userInfo.getAppId();
        String sign = SecureUtil.md5(sb);
        return StringUtils.equals(sign, callbackOrderStatusBO.getSign());
    }

    @Override
    public void postProcess(String param, PhoneOrderRecord phoneOrderRecord) {
        NewYiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, NewYiChengCallbackOrderStatusBO.class);
        TpPhoneOrderCallBack orderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, callbackOrderStatusBO.getUser_order_id()));
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
        NewYiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, NewYiChengCallbackOrderStatusBO.class);
        TpPhoneOrderCallBackLog orderLog = new TpPhoneOrderCallBackLog();
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setPlatformOrderNo(callbackOrderStatusBO.getUser_order_id())
                .setShopUserId(shopUserId)
                .setStatus(status)
                .setAppId(callbackOrderStatusBO.getSys_order_id());
        tpPhoneOrderCallBackLogService.save(orderLog);
    }

    @Override
    public String responseSuccessResult() {
        return "success";
    }

    @Override
    public String responseFailedResult() {
        return ShopCallBackStatusConstant.FAILED;
    }

    private TpPhoneOrderCallBack createTpPhoneOrderCallBack(NewYiChengCallbackOrderStatusBO bo) {
        TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
        tpPhoneOrderCallBack.setStatus(bo.getStatus().equals("200") ? 1:0);
        tpPhoneOrderCallBack.setPlatformOrderNo(bo.getUser_order_id());
        tpPhoneOrderCallBack.setShopOrderNo(bo.getSys_order_id());
        return tpPhoneOrderCallBack;
    }

}
