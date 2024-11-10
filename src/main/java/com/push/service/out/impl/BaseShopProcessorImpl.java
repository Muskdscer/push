package com.push.service.out.impl;

import com.alibaba.fastjson.JSON;
import com.push.common.controller.ResultMapInfo;
import com.push.common.enums.ShopCallbackOrderLogEnum;
import com.push.common.enums.ShopTypeEnum;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.out.BaseShopProcessor;
import com.push.service.out.ShopCommonProcessor;
import com.push.service.out.ShopProcessor;
import com.push.service.out.ShopProcessorHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.push.common.controller.BaseController.getIpAddr;

/**
 * Description:
 * Create DateTime: 2020/7/27 09:31
 *
 * 

 */
@Slf4j
@Service("baseShopProcessor")
public class BaseShopProcessorImpl implements BaseShopProcessor {

    @Resource
    private ShopProcessorHolder shopProcessorHolder;

    @Resource
    private ShopCommonProcessor shopCommonProcessor;


    //存入 TpPhoneOrderCallBack  CpTpPhoneOrderCallBac
    //回调渠道 方法内部是存redis  异步解耦
    // 回调渠道结果放入 待回调渠道订单 String PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK = "push-order:upStreamWaitCallBack";
    //redis记录商户回调状态 redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_SHOP_CALL_BACK_STATUS + phoneOrderRecord.getPlatformOrderNo(),
    // "1", 30L, TimeUnit.MINUTES);
    @Override
    public String execute(String type, Map<String, Object> paramMap) {
        String param = JSON.toJSONString(paramMap);
        log.info("【" + type + "】【商户回调】回调内容为:{}", param);

        ShopProcessor shopProcessor = shopProcessorHolder.findShopProcessor(ShopTypeEnum.getByType(type));

        //1、校验商户回调参数
        if (!shopProcessor.validateParam(param)) {
            log.error("【" + type + "】【商户回调】商户传递参数无效，参数为：{}", param);
            shopProcessor.createShopCallbackOrderLog(param, null, ShopCallbackOrderLogEnum.FAIL.getCode(), ResultMapInfo.PARAMERROR.getMessage());
            return shopProcessor.responseFailedResult();
        }

        ShopUserInfo shopUserInfo = shopProcessor.getShopUserInfo(param);

        //2、校验商户是否存在
        if (shopUserInfo == null) {
            log.error("【" + type + "】【商户回调】商户信息不存在，参数为：{}", param);
            shopProcessor.createShopCallbackOrderLog(param, null, ShopCallbackOrderLogEnum.FAIL.getCode(), ResultMapInfo.SHOP_NOT_EXIST.getMessage());
            return shopProcessor.responseFailedResult();
        }

        //3、校验商户状态
        if (!shopCommonProcessor.validateShopStatus(shopUserInfo)) {
            log.error("【" + type + "】【商户回调】商户账号被禁用，参数为：{}", param);
            shopProcessor.createShopCallbackOrderLog(param, shopUserInfo.getId(), ShopCallbackOrderLogEnum.FAIL.getCode(), ResultMapInfo.ACCOUNT_BAN.getMessage());
            return shopProcessor.responseFailedResult();
        }

        //4、校验商户IP白名单
        if (!shopCommonProcessor.validateWhiteIp(shopUserInfo.getWhiteIp(), getIpAddr())) {
            log.error("【" + type + "】【商户回调】验证白名单IP失败，参数为：{}", param);
            shopProcessor.createShopCallbackOrderLog(param, shopUserInfo.getId(), ShopCallbackOrderLogEnum.FAIL.getCode(), ResultMapInfo.LONGINIPISNULL.getMessage());
            return shopProcessor.responseFailedResult();
        }

        //5、验签
        if (!shopProcessor.validateSign(param, shopUserInfo.getAppKey())) {
            log.error("【" + type + "】【商户回调】签名验证失败，参数为：{}", param);
            shopProcessor.createShopCallbackOrderLog(param, shopUserInfo.getId(), ShopCallbackOrderLogEnum.FAIL.getCode(), ResultMapInfo.FAILESIGN.getMessage());
            return shopProcessor.responseFailedResult();
        }

        PhoneOrderRecord phoneOrderRecord = shopProcessor.getPhoneOrderRecord(param);

        //6、校验订单是否存在
        if (phoneOrderRecord == null) {
            log.error("【" + type + "】【商户回调】平台订单不存在，参数为：{}", param);
            shopProcessor.createShopCallbackOrderLog(param, shopUserInfo.getId(), ShopCallbackOrderLogEnum.FAIL.getCode(), ResultMapInfo.PLATFORMNO_NOT_EXIST.getMessage());
            return shopProcessor.responseFailedResult();
        }

        //7、校验订单是否超时
        /*if (!shopCommonProcessor.validateOrderExpire(nowDate, phoneOrderRecord.getCreateTime(), phoneOrderRecord.getOrderExpireTime(), phoneOrderRecord.getUpBackNum())) {
            log.error("【" + type + "】【商户回调】订单已超时，参数为：{}", param);
            shopProcessor.createShopCallbackOrderLog(param, shopUserInfo.getId(), ShopCallbackOrderLogEnum.FAIL.getCode(), ResultMapInfo.ORDERTIMEOUT.getMessage());
            return ShopCallBackStatusConstant.FAILED.toUpperCase();
        }*/

        //8、完成校验，进入后置处理
        try {
            //存入 TpPhoneOrderCallBack  CpTpPhoneOrderCallBack
            //回调渠道 方法内部是存redis  异步解耦
            // 回调渠道结果放入 待回调渠道订单 String PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK = "push-order:upStreamWaitCallBack";
            //redis记录商户回调状态 redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_SHOP_CALL_BACK_STATUS + phoneOrderRecord.getPlatformOrderNo(),
            // "1", 30L, TimeUnit.MINUTES);
            shopProcessor.postProcess(param, phoneOrderRecord);
        } catch (Exception e) {
            log.error("【商户回调】出现异常,商户回调参数:{},异常信息:", param, e);
            return shopProcessor.responseFailedResult();
        }

        log.info("【" + type + "】【商户回调】订单号:{}, 记录完成", phoneOrderRecord.getPlatformOrderNo());

        return shopProcessor.responseSuccessResult();
    }
}
