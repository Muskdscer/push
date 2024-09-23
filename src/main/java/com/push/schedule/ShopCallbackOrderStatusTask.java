package com.push.schedule;

import com.push.service.schedule.ShopCallbackOrderStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020/4/1 18:29
 *
 * 

 */
@Slf4j
@Component
public class ShopCallbackOrderStatusTask {

    @Resource
    private ShopCallbackOrderStatusService shopCallbackOrderStatusService;

    /**
     * 消费商户回调成功消息
     * <p>
     * 每5秒执行一次
     */
    @Scheduled(fixedDelay = 1000)
    public void handleShopCallbackSuccess() {
//        log.info("【消费商户回调成功】触发开始：{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        shopCallbackOrderStatusService.handleShopCallbackSuccessOrder();
//        log.info("【消费商户回调成功】触发结束：{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 消费商户回调失败消息
     * <p>
     * 每5秒执行一次
     */
    @Scheduled(fixedDelay = 1500)
    public void handleShopCallbackFail() {
//        log.info("【消费商户回调失败】触发开始：{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        shopCallbackOrderStatusService.handleShopCallbackFailOrder();
//        log.info("【消费商户回调失败】触发结束：{}", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

}
