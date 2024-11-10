package com.push.service.out;

import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020/7/27 09:31
 *
 * 

 */
public interface BaseShopProcessor {

    //存入 TpPhoneOrderCallBack  CpTpPhoneOrderCallBac
    //回调渠道 方法内部是存redis  异步解耦
    // 回调渠道结果放入 待回调渠道订单 String PUSH_ORDER_UP_STREAM_WAIT_CALL_BACK = "push-order:upStreamWaitCallBack";
    //redis记录商户回调状态 redisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_SHOP_CALL_BACK_STATUS + phoneOrderRecord.getPlatformOrderNo(),
    // "1", 30L, TimeUnit.MINUTES);
    String execute(String type, Map<String, Object> paramMap);

}
