package com.push.schedule;

import com.push.service.schedule.QueryOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020-04-02 15:20
 *
 *

 */
@Slf4j
@Component
public class QueryOrderTask {

    @Resource
    private QueryOrderService queryOrderService;


    /**
     * 处理超时订单检查成功队列
     */
    @Scheduled(fixedDelay = 1000)
    public void handlerWaitCheckOrderSuccess() {
        queryOrderService.handlerWaitCheckOrderSuccess();
    }

    /**
     * 处理超时订单检查失败（异常）队列
     */
    @Scheduled(fixedDelay = 1500)
    public void handlerWaitCheckOrderFailed() {
        queryOrderService.handlerWaitCheckOrderFailed();
    }

    /**
     * 处理超时订单检查处理中队列
     */
    @Scheduled(fixedDelay = 1000)
    public void handlerWaitCheckOrderProcessing() {
        queryOrderService.handlerWaitCheckOrderProcessing();
    }

    /**
     * 定时扫描超时订单表状态变更
     */
    @Scheduled(fixedDelay = 1000)
    public void handlerWaitCheckOrder() {
        queryOrderService.checkOrderTimeOut();
    }

    @Scheduled(fixedDelay = 30000)
    public void handlerScanWaitOrderException() {
        queryOrderService.waitCheckOrderException();
    }
}
