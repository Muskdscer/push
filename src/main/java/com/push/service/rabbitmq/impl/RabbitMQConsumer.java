package com.push.service.rabbitmq.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.push.common.constants.RabbitMQConstant;
import com.push.common.constants.RedisConstant;
import com.push.common.webfacade.vo.out.MqMessage;
import com.push.common.webfacade.vo.platform.PhoneOrderWaitCheck;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Description: RabbitMQ 消费端
 * Create DateTime: 2020-03-30 18:14
 *
 *

 */
@Slf4j
@Component
public class RabbitMQConsumer {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @RabbitListener(queues = RabbitMQConstant.RABBIT_DELAY_ORDER_TIME_OUT_QUEUE)
    public void handlerOrderWithTimeOut(Message message, Channel channel) throws IOException {
        MqMessage mqMessage = JSONObject.parseObject(String.valueOf(message.getPayload()), MqMessage.class);

        String hasSendKey = RedisConstant.PUSH_ORDER_WAIT_PUSH_SUCCESS + mqMessage.getPlatformOrderNo();

        //判断订单是否成功发送商户
        Boolean hasSend = redisTemplate.hasKey(hasSendKey);
        redisTemplate.delete(hasSendKey);
        log.info("【MQ消费者】超时订单：{}, 是否推送商户成功：{}", mqMessage.getPlatformOrderNo(), hasSend);

        if (hasSend) {
            String key = RedisConstant.PUSH_ORDER_SHOP_CALL_BACK_STATUS + mqMessage.getPlatformOrderNo();

            //判断商户是否回调
            Boolean hasKey = redisTemplate.hasKey(key);
            redisTemplate.delete(key);

            log.info("【MQ消费者】超时订单：{}, 商户是否回调：{}", mqMessage.getPlatformOrderNo(), hasKey);
            if (!hasKey) {
                log.info("【MQ消费者】超时订单：{}, 进行超时查询", mqMessage.getPlatformOrderNo());
                PhoneOrderWaitCheck orderWaitCheck = new PhoneOrderWaitCheck(mqMessage.getQuerySite(), mqMessage.getPlatformOrderNo()
                        , mqMessage.getClassifyName(), mqMessage.getAppId(), mqMessage.getAppKey());
                redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_WAIT_CHECK, JSON.toJSONString(orderWaitCheck));
            }
        }
        ack(message, channel);
    }

    /**
     * 手动确认消息
     *
     * @param message 消息
     * @param channel 队列
     */
    private void ack(Message message, Channel channel) throws IOException {
        Long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
        channel.basicAck(deliveryTag, false);
    }
}
