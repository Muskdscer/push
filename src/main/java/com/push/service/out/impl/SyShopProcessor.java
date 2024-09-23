package com.push.service.out.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.SyCallbackOrderStatusBO;
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
 * Create DateTime: 2020/6/10 15:25
 *
 * 

 */
@Slf4j
@Service
public class SyShopProcessor implements ShopProcessor {

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
        SyCallbackOrderStatusBO syCallbackOrderStatusBO = JSON.parseObject(param, SyCallbackOrderStatusBO.class);
        return shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getAppId, syCallbackOrderStatusBO.getCoopId()));
    }

    @Override
    public boolean validateParam(String param) {
        SyCallbackOrderStatusBO syCallbackOrderStatusBO = JSON.parseObject(param, SyCallbackOrderStatusBO.class);
        return !StringUtils.isBlank(syCallbackOrderStatusBO.getTranid())
                && !StringUtils.isBlank(syCallbackOrderStatusBO.getResponseType())
                && syCallbackOrderStatusBO.getFailedCode() != null;
    }

    @Override
    public PhoneOrderRecord getPhoneOrderRecord(String param) {
        SyCallbackOrderStatusBO syCallbackOrderStatusBO = JSON.parseObject(param, SyCallbackOrderStatusBO.class);
        return phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, syCallbackOrderStatusBO.getTranid()));
    }

    @Override
    public boolean validateSign(String param, String appKey) {
        SyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, SyCallbackOrderStatusBO.class);

        TreeMap<String, String> data = new TreeMap<>();
        data.put("coopId", appKey);
        data.put("tranid", callbackOrderStatusBO.getTranid());
        data.put("responseType", callbackOrderStatusBO.getResponseType());
        if (StringUtils.isNotBlank(callbackOrderStatusBO.getOrderNo())) {
            data.put("orderNo", callbackOrderStatusBO.getOrderNo());
        }
        if (StringUtils.isNotBlank(callbackOrderStatusBO.getFailedReason())) {
            data.put("failedReason", callbackOrderStatusBO.getFailedReason());
        }
        data.put("failedCode", callbackOrderStatusBO.getFailedCode());

        String sign = SignUtils.syGenerateSignature(data, SignTypeEnum.MD5, appKey);
        return StringUtils.equals(sign, callbackOrderStatusBO.getSign());
    }

    @Override
    public void postProcess(String param, PhoneOrderRecord phoneOrderRecord) {
        SyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, SyCallbackOrderStatusBO.class);

        //防止商户多次回调相同订单
        TpPhoneOrderCallBack tpPhoneOrderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, callbackOrderStatusBO.getTranid()));
        if (tpPhoneOrderCallBack != null) {
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
        SyCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, SyCallbackOrderStatusBO.class);
        TpPhoneOrderCallBackLog orderLog = new TpPhoneOrderCallBackLog();
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setPlatformOrderNo(callbackOrderStatusBO.getTranid())
                .setShopUserId(shopUserId)
                .setStatus(status)
                .setAppId(callbackOrderStatusBO.getCoopId());
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

    private TpPhoneOrderCallBack createTpPhoneOrderCallBack(SyCallbackOrderStatusBO bo) {
        TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
        tpPhoneOrderCallBack.setStatus(bo.getFailedCode().equals("1") ? 1 : 0);
        tpPhoneOrderCallBack.setPlatformOrderNo(bo.getTranid());
        tpPhoneOrderCallBack.setOrderSn(bo.getOrderNo());
        tpPhoneOrderCallBack.setMessage(bo.getFailedReason());
        tpPhoneOrderCallBack.setShopOrderNo(bo.getTranid());
        return tpPhoneOrderCallBack;
    }
}
