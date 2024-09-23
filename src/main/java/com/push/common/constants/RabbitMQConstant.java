package com.push.common.constants;

/**
 * Description: RabbitMQ常量配置
 * Create DateTime: 2020-03-30 09:49
 *
 *

 */
public interface RabbitMQConstant {

    //=============================Direct=======================================

    /**
     * direct 交换机
     */
    String RABBIT_DIRECT_EXCHANGE = "push_order_direct_exchange";

    /**
     * direct 队列
     */
    String RABBIT_DIRECT_QUEUE = "push_order_direct_queue";

    /**
     * direct routing key
     */
    String RABBIT_DIRECT_ROUTING_KEY = "push_order_direct_key";

    //===========================================================================

    //=============================Delayed=======================================

    /**
     * 延时队列交换机
     */
    String RABBIT_DELAY_EXCHANGE = "push_order_delay_exchange";

    /**
     * 延时队列交换机类型
     */
    String RABBIT_DELAY_EXCHANGE_TYPE = "x-delayed-message";

    /**
     * 超时订单延时队列
     */
    String RABBIT_DELAY_ORDER_TIME_OUT_QUEUE = "push_order_time_out_delay_queue";

    /**
     * 超时订单延时队列 routing key
     */
    String RABBIT_DELAY_ORDER_TIME_OUT_ROUTING_KEY = "push_order_time_out_delay_key";

    //===========================================================================

}
