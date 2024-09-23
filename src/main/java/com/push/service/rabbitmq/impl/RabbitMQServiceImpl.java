package com.push.service.rabbitmq.impl;

import com.alibaba.fastjson.JSON;
import com.push.service.rabbitmq.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: RabbitMQ service接口实现类
 * Create DateTime: 2020-03-30 10:00
 *
 * 

 */
@Slf4j
@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendSimpleMessage(String exchange, String routingKey, Object data) {
        rabbitTemplate.convertAndSend(exchange, routingKey, JSON.toJSONString(data));
    }

    @Override
    public void sendDelayMessage(String exchange, String routingKey, Object data, Integer delayTime) {
        rabbitTemplate.convertAndSend(exchange, routingKey, JSON.toJSONString(data), message -> {
            message.getMessageProperties().setDelay(delayTime * 1000);
            return message;
        });
    }
}
