package com.push.config.rabbitmq;

import com.push.common.constants.RabbitMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: RabbitMQ 配置
 * Create DateTime: 2020-03-28 18:52
 *
 *

 */
@Slf4j
@Configuration
public class RabbitMQConfig {

    //===================================交换机声明==========================================

    /**
     * 声明direct交换机
     */
    @Bean
    public Exchange directExchange() {
        return new DirectExchange(RabbitMQConstant.RABBIT_DIRECT_EXCHANGE, true, false);
    }

    /**
     * 声明延时交换机
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>(2);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQConstant.RABBIT_DELAY_EXCHANGE,
                RabbitMQConstant.RABBIT_DELAY_EXCHANGE_TYPE, true, false, args);
    }

    //=====================================队列声明=============================================

    /**
     * 声明direct队列
     */
    @Bean
    public Queue directQueue() {
        return new Queue(RabbitMQConstant.RABBIT_DIRECT_QUEUE, true);
    }

    /**
     * 声明订单超时延时队列
     */
    @Bean
    public Queue delayOrderTimeOutQueue() {
        return new Queue(RabbitMQConstant.RABBIT_DELAY_ORDER_TIME_OUT_QUEUE, true);
    }

    //=====================================绑定队列到交换机====================================

    /**
     * 绑定direct队列到direct交换机
     */
    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(RabbitMQConstant.RABBIT_DIRECT_ROUTING_KEY).noargs();
    }

    /**
     * 绑定delay订单超时队列到delay交换机
     */
    @Bean
    public Binding delayOrderTimeOutBinding() {
        return BindingBuilder.bind(delayOrderTimeOutQueue()).to(delayExchange()).with(RabbitMQConstant.RABBIT_DELAY_ORDER_TIME_OUT_ROUTING_KEY).noargs();
    }

}
