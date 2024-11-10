package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderRecord;

/**
 * <p>
 * 可用订单表 服务类
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface PhoneOrderAvailableService extends IService<PhoneOrderAvailable> {

    //渠道推单过来 根据渠道 通道找商户 拿着所有用户id去查询所有推送配比对象 拿着商户i查ShopPushMatch
    //渠道推订单过来 String PUSH_ORDER_WAIT_PUSH = "push-order:waitPush";
    //availableOrder落库
    //redis队列放phoneOrderWaitPush  待推送订单 String PUSH_ORDER_WAIT_PUSH = "push-order:waitPush";
    //如果不是慢充 延时队列交换机 String RABBIT_DELAY_EXCHANGE = "push_order_delay_exchange";
    //如果不是慢充  超时订单延时队列 routing key String RABBIT_DELAY_ORDER_TIME_OUT_ROUTING_KEY = "push_order_time_out_delay_key";
    //拷贝availableOrder到PhoneOrderRecord   PhoneOrderRecord落库
    //推送数量  根据商户 运营商 订单价格 推送数量 每一次加一String key = RedisConstant.CURRENT_PUSH_NUM + date + ":" + userInfo.getId() + ":"
    // + availableOrder.getPhoneOperator() + ":" + availableOrder.getOrderPrice();
    Boolean saveBatchByAvailableAndSaveBatchByRecord(PhoneOrderAvailable availableOrders,
                                                     PhoneOrderRecord recordOrders,
                                                     Long upstreamAisleClassifyId,
                                                     Integer rechargeType);

}
