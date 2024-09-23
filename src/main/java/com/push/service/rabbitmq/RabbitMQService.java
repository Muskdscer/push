package com.push.service.rabbitmq;

/**
 * Description: RabbitMQ service接口
 * Create DateTime: 2020-03-30 10:00
 *
 * 

 */
public interface RabbitMQService {

    /**
     * 发送单一消息
     *
     * @param exchange   交换机
     * @param routingKey routingKey
     * @param data       需发送的数据
     */
    void sendSimpleMessage(String exchange, String routingKey, Object data);

    /**
     * 发送延迟消息
     *
     * @param exchange   交换机
     * @param routingKey routingKey
     * @param data       需发送的数据
     * @param delayTime  延迟时间（毫秒）
     */
    void sendDelayMessage(String exchange, String routingKey, Object data, Integer delayTime);

}
