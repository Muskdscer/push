package com.push.service.out.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.SjCallbackOrderStatusBO;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Create DateTime: 2020/7/24 18:23
 *
 *

 */
@Slf4j
@Service
public class SjShopProcessor implements ShopProcessor {

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
        SjCallbackOrderStatusBO sjCallbackOrderStatusBO = JSON.parseObject(param, SjCallbackOrderStatusBO.class);
        PhoneOrderRecord orderRecord = phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, sjCallbackOrderStatusBO.getTradeNoThird()).select(PhoneOrderRecord::getShopUserId));
        return shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getId, orderRecord.getShopUserId()));
    }

    @Override
    public boolean validateParam(String param) {
        SjCallbackOrderStatusBO sjCallbackOrderStatusBO = JSON.parseObject(param, SjCallbackOrderStatusBO.class);
        return sjCallbackOrderStatusBO != null
                && StringUtils.isNotBlank(sjCallbackOrderStatusBO.getTradeNoThird())
                && StringUtils.isNotBlank(sjCallbackOrderStatusBO.getSignstr());
    }

    @Override
    public PhoneOrderRecord getPhoneOrderRecord(String param) {
        SjCallbackOrderStatusBO sjCallbackOrderStatusBO = JSON.parseObject(param, SjCallbackOrderStatusBO.class);
        return phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, sjCallbackOrderStatusBO.getTradeNoThird()));
    }

    @Override
    public boolean validateSign(String param, String appKey) {
        SjCallbackOrderStatusBO sjCallbackOrderStatusBO = JSON.parseObject(param, SjCallbackOrderStatusBO.class);

        StringBuilder sb = new StringBuilder();
        sb.append(sjCallbackOrderStatusBO.getCode())
                .append(sjCallbackOrderStatusBO.getTradeNoThird())
                .append(new String(sjCallbackOrderStatusBO.getMsg().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8))
                .append(StringUtils.isNotBlank(sjCallbackOrderStatusBO.getTradeNo()) ? sjCallbackOrderStatusBO.getTradeNo() : "")
                .append(appKey);
        String sign = SecureUtil.md5(sb.toString());
        return StringUtils.equals(sign, sjCallbackOrderStatusBO.getSignstr());
    }

    @Override
    public void postProcess(String param, PhoneOrderRecord phoneOrderRecord) {
        SjCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, SjCallbackOrderStatusBO.class);

        TpPhoneOrderCallBack orderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, callbackOrderStatusBO.getTradeNoThird()));

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
        SjCallbackOrderStatusBO sjCallbackOrderStatusBO = JSON.parseObject(param, SjCallbackOrderStatusBO.class);
        TpPhoneOrderCallBackLog orderLog = new TpPhoneOrderCallBackLog();
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setPlatformOrderNo(sjCallbackOrderStatusBO.getTradeNoThird())
                .setShopUserId(shopUserId)
                .setStatus(status)
                .setAppId("Sj");
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

    private TpPhoneOrderCallBack createTpPhoneOrderCallBack(SjCallbackOrderStatusBO bo) {
        TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
        tpPhoneOrderCallBack.setStatus(bo.getCode() == 200 ? 1 : 0);
        tpPhoneOrderCallBack.setPlatformOrderNo(bo.getTradeNoThird());
        tpPhoneOrderCallBack.setOrderSn(bo.getTradeNo());
        tpPhoneOrderCallBack.setMessage(bo.getMsg());
        return tpPhoneOrderCallBack;
    }
}
