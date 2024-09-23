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
public class HappyShopProcessor implements ShopProcessor {

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
        HappyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HappyCallbackOrderStatusBO.class);
        return shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class).eq(ShopUserInfo::getAppId, callbackOrderStatusBO.getMchid()));
    }

    @Override
    public boolean validateParam(String param) {
        HappyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HappyCallbackOrderStatusBO.class);
        return StringUtils.isNotBlank(callbackOrderStatusBO.getOrder_id())
                && StringUtils.isNotBlank(callbackOrderStatusBO.getOut_order_id())
                && StringUtils.isNotBlank(callbackOrderStatusBO.getSign())
                && callbackOrderStatusBO.getStatus() != null;
    }

    @Override
    public PhoneOrderRecord getPhoneOrderRecord(String param) {
        HappyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HappyCallbackOrderStatusBO.class);
        return phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, callbackOrderStatusBO.getOrder_id()));
    }

    @Override
    public boolean validateSign(String param, String appKey) {
        HappyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HappyCallbackOrderStatusBO.class);
        String sb = callbackOrderStatusBO.getOrder_id() + callbackOrderStatusBO.getOut_order_id()
                + callbackOrderStatusBO.getMchid() + callbackOrderStatusBO.getTel()
                + callbackOrderStatusBO.getPrice() + callbackOrderStatusBO.getStatus()
                + appKey;
        String sign = SecureUtil.md5(sb);
        return StringUtils.equals(sign, callbackOrderStatusBO.getSign());
    }

    @Override
    public void postProcess(String param, PhoneOrderRecord phoneOrderRecord) {
        HappyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HappyCallbackOrderStatusBO.class);
        TpPhoneOrderCallBack orderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, callbackOrderStatusBO.getOrder_id()));
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
        HappyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HappyCallbackOrderStatusBO.class);
        TpPhoneOrderCallBackLog orderLog = new TpPhoneOrderCallBackLog();
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setPlatformOrderNo(callbackOrderStatusBO.getOrder_id())
                .setShopUserId(shopUserId)
                .setStatus(status)
                .setAppId(callbackOrderStatusBO.getMchid());
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

    private TpPhoneOrderCallBack createTpPhoneOrderCallBack(HappyCallbackOrderStatusBO bo) {
        TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
        tpPhoneOrderCallBack.setStatus(bo.getStatus().equals("1") ? 1:0);
        tpPhoneOrderCallBack.setPlatformOrderNo(bo.getOrder_id());
        tpPhoneOrderCallBack.setShopOrderNo(bo.getOrder_id());
        return tpPhoneOrderCallBack;
    }

}
