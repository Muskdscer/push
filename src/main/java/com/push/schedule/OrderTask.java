package com.push.schedule;

import com.alibaba.fastjson.JSONObject;
import com.push.common.constants.RedisConstant;
import com.push.common.webfacade.bo.out.SjShopOrderNoBO;
import com.push.common.webfacade.vo.out.PhoneOrderWaitPush;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.service.schedule.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/4/2 15:25
 *
 * 

 */
@Slf4j
@Component
public class OrderTask {

    @Value("${handler.count}")
    private Long count;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private OrderService orderService;

    /**
     * 处理推送订单失败队列 (真正失败的)
     */
    @Scheduled(fixedDelay = 1500)
    public void updateReturnStatus() {
        // 获取待处理数据
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_WAIT_PUSH_RECORD);
        if (size == null || size == 0) {
            return;
        }
        if (size >= count) {
            size = this.count;
        }
        List<PhoneOrderAvailable> phoneOrderNos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Object obj = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_WAIT_PUSH_RECORD);
            if (obj != null) {
                PhoneOrderWaitPush phoneOrderWaitPush = JSONObject.parseObject(obj.toString(), PhoneOrderWaitPush.class);
                phoneOrderNos.add(phoneOrderWaitPush.getPhoneOrderAvailable());
            }
        }
        phoneOrderNos.forEach(order -> {
            log.info("【处理推送订单——真正失败】订单号为：{}", order.getPlatformOrderNo());
            orderService.handlerFailedOrder(order, false);
        });
    }

    /**
     * 处理推送订单异常队列（Read TimeOut）
     */
    @Scheduled(fixedDelay = 1500)
    public void handlerPushOrderException() {
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_WAIT_PUSH_EXCEPTION);
        if (size == null || size == 0) {
            return;
        }
        if (size >= count) {
            size = this.count;
        }

        List<PhoneOrderAvailable> phoneOrderNos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Object obj = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_WAIT_PUSH_EXCEPTION);
            if (obj != null) {
                PhoneOrderWaitPush phoneOrderWaitPush = JSONObject.parseObject(obj.toString(), PhoneOrderWaitPush.class);
                phoneOrderNos.add(phoneOrderWaitPush.getPhoneOrderAvailable());
            }
        }
        phoneOrderNos.forEach(order -> {
            log.info("【处理推送订单——Read TimeOut】订单号为：{}", order.getPlatformOrderNo());
            orderService.handlerFailedOrder(order, true);
        });
    }

    /**
     * 处理SJ下单成功返回的商户订单号
     */
    @Scheduled(fixedDelay = 1000)
    public void handlerPushOrderSuccessForSJ() {
        Long size = redisTemplate.opsForList().size(RedisConstant.PUSH_ORDER_SJ_SUCCESS_WITH_SHOP_ORDER_NO);
        if (size == null || size == 0) {
            return;
        }
        if (size >= count) {
            size = this.count;
        }

        List<SjShopOrderNoBO> sjShopOrderNoBOList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Object obj = redisTemplate.opsForList().rightPop(RedisConstant.PUSH_ORDER_SJ_SUCCESS_WITH_SHOP_ORDER_NO);
            if (obj != null) {
                SjShopOrderNoBO sjShopOrderNoBO = JSONObject.parseObject(obj.toString(), SjShopOrderNoBO.class);
                sjShopOrderNoBOList.add(sjShopOrderNoBO);
            }
        }
        orderService.handlerPushOrderSuccessForSJ(sjShopOrderNoBOList);
    }
}
