package com.push.service.schedule;

/**
 * Description:
 * Create DateTime: 2020-04-02 15:23
 *
 * 

 */
public interface QueryOrderService {

    /**
     * 处理待查询订单响应
     */
    void handlerWaitCheckOrderSuccess();

    /**
     * 处理待查询订单响应失败
     */
    void handlerWaitCheckOrderFailed();

    /**
     * 处理处理中订单
     */
    void handlerWaitCheckOrderProcessing();

    /**
     * 检查超时订单
     */
    void checkOrderTimeOut();

    /**
     *
     */
    void waitCheckOrderException();
}
