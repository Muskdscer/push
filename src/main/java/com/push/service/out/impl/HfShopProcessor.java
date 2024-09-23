package com.push.service.out.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.constants.ShopCallBackStatusConstant;
import com.push.common.enums.SignTypeEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.SignUtils;
import com.push.common.webfacade.bo.out.HfCallbackOrderStatusBO;
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
 * Create DateTime: 2020/5/22 10:09
 *
 *

 */
@Slf4j
@Service
public class HfShopProcessor implements ShopProcessor {

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
        HfCallbackOrderStatusBO hfCallbackOrderStatusBO = JSON.parseObject(param, HfCallbackOrderStatusBO.class);
        return shopUserInfoService.getOne(Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getAppId, hfCallbackOrderStatusBO.getApi_code()));
    }

    @Override
    public boolean validateParam(String param) {
        HfCallbackOrderStatusBO hfCallbackOrderStatusBO = JSON.parseObject(param, HfCallbackOrderStatusBO.class);
        return !StringUtils.isBlank(hfCallbackOrderStatusBO.getOrderid())
                && !StringUtils.isBlank(hfCallbackOrderStatusBO.getCpparam())
                && hfCallbackOrderStatusBO.getStatus() != null;
    }

    @Override
    public PhoneOrderRecord getPhoneOrderRecord(String param) {
        HfCallbackOrderStatusBO hfCallbackOrderStatusBO = JSON.parseObject(param, HfCallbackOrderStatusBO.class);
        return phoneOrderRecordService.getOne(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .eq(PhoneOrderRecord::getPlatformOrderNo, hfCallbackOrderStatusBO.getCpparam()));
    }

    @Override
    public boolean validateSign(String param, String appKey) {
        HfCallbackOrderStatusBO hfCallbackOrderStatusBO = JSON.parseObject(param, HfCallbackOrderStatusBO.class);

        TreeMap<String, String> data = new TreeMap<>();
        data.put("a", appKey);
        data.put("b", hfCallbackOrderStatusBO.getStatus().toString());
        data.put("c", hfCallbackOrderStatusBO.getMessage());
        if (hfCallbackOrderStatusBO.getSpref_url() != null && !hfCallbackOrderStatusBO.getSpref_url().equals("")) {
            data.put("d", hfCallbackOrderStatusBO.getSpref_url());
        }
        data.put("e", hfCallbackOrderStatusBO.getOrderid());
        data.put("f", hfCallbackOrderStatusBO.getCpparam());
        data.put("g", appKey);

        String sign = SignUtils.generateSignature(data, SignTypeEnum.MD5);
        return StringUtils.equals(sign, hfCallbackOrderStatusBO.getSign());
    }

    @Override
    public void postProcess(String param, PhoneOrderRecord phoneOrderRecord) {
        HfCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HfCallbackOrderStatusBO.class);

        TpPhoneOrderCallBack orderCallBack = tpPhoneOrderCallBackService.getOne(Wrappers.lambdaQuery(TpPhoneOrderCallBack.class)
                .eq(TpPhoneOrderCallBack::getPlatformOrderNo, callbackOrderStatusBO.getCpparam()));

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
        HfCallbackOrderStatusBO callbackOrderStatusBO = JSON.parseObject(param, HfCallbackOrderStatusBO.class);
        TpPhoneOrderCallBackLog orderLog = new TpPhoneOrderCallBackLog();
        orderLog.setPushNum(1)
                .setRemark(remark)
                .setPlatformOrderNo(callbackOrderStatusBO.getCpparam())
                .setShopUserId(shopUserId)
                .setStatus(status)
                .setAppId(callbackOrderStatusBO.getApi_code());
        tpPhoneOrderCallBackLogService.save(orderLog);
    }

    @Override
    public String responseSuccessResult() {
        return ShopCallBackStatusConstant.SUCCESSFUL.toUpperCase();
    }

    @Override
    public String responseFailedResult() {
        return ShopCallBackStatusConstant.FAILED.toUpperCase();
    }

    private TpPhoneOrderCallBack createTpPhoneOrderCallBack(HfCallbackOrderStatusBO bo) {
        TpPhoneOrderCallBack tpPhoneOrderCallBack = new TpPhoneOrderCallBack();
        tpPhoneOrderCallBack.setStatus(bo.getStatus());
        tpPhoneOrderCallBack.setPlatformOrderNo(bo.getCpparam());
        tpPhoneOrderCallBack.setShopOrderNo(bo.getOrderid());
        tpPhoneOrderCallBack.setOrderSn(bo.getOrder_no());
        tpPhoneOrderCallBack.setCertificate(bo.getSpref_url());
        tpPhoneOrderCallBack.setMessage(bo.getMessage());
        return tpPhoneOrderCallBack;
    }
}
