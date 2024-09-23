package com.push.service.out.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.convert.YiChengOrderStatusConvert;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.YiChengCallbackOrderStatusBO;
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
public class YiChengShopProcessor implements ShopProcessor {

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
        YiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, YiChengCallbackOrderStatusBO.class);
        return shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class).eq(ShopUserInfo::getAppId, callbackOrderStatusBO.getUserId()));
    }

    @Override
    public boolean validateParam(String param) {
        YiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, YiChengCallbackOrderStatusBO.class);
        return !StringUtils.isBlank(callbackOrderStatusBO.getEjId())
                && !StringUtils.isBlank(callbackOrderStatusBO.getDownstreamSerialno())
                && callbackOrderStatusBO.getStatus() != null;
    }

    @Override
    public PhoneOrderRecord getPhoneOrderRecord(String param) {
        YiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, YiChengCallbackOrderStatusBO.class);
        return phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, callbackOrderStatusBO.getDownstreamSerialno()));
    }

    @Override
    public boolean validateSign(String param, String appKey) {
        YiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, YiChengCallbackOrderStatusBO.class);

        TreeMap<String, String> data = new TreeMap<>();
        data.put("a", callbackOrderStatusBO.getBizId());
        data.put("b", callbackOrderStatusBO.getDownstreamSerialno());
        data.put("c", callbackOrderStatusBO.getEjId());
        data.put("d", callbackOrderStatusBO.getStatus());
        data.put("e", callbackOrderStatusBO.getUserId());
        data.put("f", appKey);

        String sign = SignUtils.generateSignature(data, SignTypeEnum.MD5);
        return StringUtils.equals(sign, callbackOrderStatusBO.getSign());
    }

    @Override
    public void postProcess(String param, PhoneOrderRecord phoneOrderRecord) {
        YiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, YiChengCallbackOrderStatusBO.class);

        TpPhoneOrderCallBack orderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, callbackOrderStatusBO.getDownstreamSerialno()));

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
        YiChengCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, YiChengCallbackOrderStatusBO.class);
        TpPhoneOrderCallBackLog orderLog = new TpPhoneOrderCallBackLog();
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setPlatformOrderNo(callbackOrderStatusBO.getDownstreamSerialno())
                .setShopUserId(shopUserId)
                .setStatus(status)
                .setAppId(callbackOrderStatusBO.getUserId());
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

    private TpPhoneOrderCallBack createTpPhoneOrderCallBack(YiChengCallbackOrderStatusBO bo) {
        TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
        tpPhoneOrderCallBack.setStatus(YiChengOrderStatusConvert.convertStatus(bo.getStatus()));
        tpPhoneOrderCallBack.setPlatformOrderNo(bo.getDownstreamSerialno());
        tpPhoneOrderCallBack.setShopOrderNo(bo.getEjId());
        tpPhoneOrderCallBack.setOrderSn(bo.getVoucher());
        tpPhoneOrderCallBack.setMessage("");
        return tpPhoneOrderCallBack;
    }
}
