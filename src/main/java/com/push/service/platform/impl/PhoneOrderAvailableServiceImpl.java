package com.push.service.platform.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.common.constants.ParamConstants;
import com.push.common.constants.RabbitMQConstant;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.enums.RechargeTypeEnum;
import com.push.common.enums.UserStatusEnum;
import com.push.common.utils.BeanUtils;
import com.push.common.webfacade.vo.out.MqMessage;
import com.push.common.webfacade.vo.out.PhoneOrderWaitPush;
import com.push.config.system.SystemConfig;
import com.push.dao.mapper.platform.AisleMatchMapper;
import com.push.dao.mapper.platform.PhoneOrderAvailableMapper;
import com.push.dao.mapper.shop.ShopPushMatchMapper;
import com.push.dao.mapper.shop.ShopUserMapper;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.ShopMatchClassify;
import com.push.entity.shop.ShopPushMatch;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.platform.PhoneOrderAvailableService;
import com.push.service.platform.PhoneOrderRecordService;
import com.push.service.platform.ShopMatchClassifyService;
import com.push.service.rabbitmq.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 可用订单表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Slf4j
@Service
public class PhoneOrderAvailableServiceImpl extends ServiceImpl<PhoneOrderAvailableMapper, PhoneOrderAvailable> implements PhoneOrderAvailableService {

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;

    @Resource
    private ShopPushMatchMapper shopPushMatchMapper;

    @Resource
    private ShopUserMapper shopUserMapper;

    @Resource
    private AisleMatchMapper aisleMatchMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Integer> intRedisTemplate;

    @Resource
    private RabbitMQService rabbitMQService;

    @Resource
    private ShopMatchClassifyService shopMatchClassifyService;

    //渠道推单过来 根据渠道 通道找商户 拿着所有用户id去查询所有推送配比对象 拿着商户i查ShopPushMatch
    //渠道推订单过来 String PUSH_ORDER_WAIT_PUSH = "push-order:waitPush";
    //availableOrder落库
    //redis队列放phoneOrderWaitPush  待推送订单 String PUSH_ORDER_WAIT_PUSH = "push-order:waitPush";
    //如果不是慢充 延时队列交换机 String RABBIT_DELAY_EXCHANGE = "push_order_delay_exchange";
    //如果不是慢充  超时订单延时队列 routing key String RABBIT_DELAY_ORDER_TIME_OUT_ROUTING_KEY = "push_order_time_out_delay_key";
    //拷贝availableOrder到PhoneOrderRecord   PhoneOrderRecord落库
    //推送数量  根据商户 运营商 订单价格 推送数量 每一次加一String key = RedisConstant.CURRENT_PUSH_NUM + date + ":" + userInfo.getId() + ":"
    // + availableOrder.getPhoneOperator() + ":" + availableOrder.getOrderPrice();
    @Override
    @TransactionalWithRollback
    public Boolean saveBatchByAvailableAndSaveBatchByRecord(PhoneOrderAvailable availableOrder, PhoneOrderRecord recordOrder,
                                                            Long aisleClassifyId, Integer rechargeType) {
        //拿到此渠道用户对应的商户id
        List<Long> ids = aisleMatchMapper.selectShopIds(aisleClassifyId, DeleteFlagEnum.NOT_DELETE.getCode(), UserStatusEnum.ENABLE.getValue());
        if (CollectionUtils.isEmpty(ids)) {
            log.info("【渠道推单】该渠道无下游配比：id:{}, name:{}", recordOrder.getUpstreamUserId(), recordOrder.getUpstreamName());
            return false;
        }
        //List<Long> ids = aisleMatches.stream().map(AisleMatch::getShopId).collect(Collectors.toList());
        //查询所有未删除并且推送开关开的并且有推送地址的用户id
        /*List<Long> ids = shopUserMapper.selectList(Wrappers
                .lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode())
                .eq(ShopUserInfo::getPushSwitch, DeleteFlagEnum.NOT_DELETE.getCode())//此处删除枚举当做推送开关使用
                .eq(ShopUserInfo::getMatchClassifyId, matchClassifyId)
                .eq(ShopUserInfo::getStatus, UserStatusEnum.ENABLE.getValue())
                .isNotNull(ShopUserInfo::getPushSite))
                .stream()
                .map(ShopUserInfo::getId)
                .collect(Collectors.toList());*/
        List<ShopPushMatch> collect = shopPushMatchMapper.selectByShopIds(ids, DeleteFlagEnum.NOT_DELETE.getCode(),
                availableOrder.getOrderPrice(), availableOrder.getPhoneOperator(), aisleClassifyId);
        long count = collect.stream().mapToLong(ShopPushMatch::getMatchNum).sum();
        double random = Math.random() * count + 1;
        Optional<ShopPushMatch> shopPushMatch = collect.stream()
                .filter(a -> a.getEnd() - a.getStart() > 0)
                .filter((a) -> a.getStart() <= random && a.getEnd() + 1 >= random).findAny();
        Optional<ShopPushMatch> any = collect.stream().findAny();
        ShopPushMatch pushMatch = new ShopPushMatch();
        if (any.isPresent()) {
            pushMatch = any.get();
        }
        if (any.isEmpty() && shopPushMatch.isEmpty()) {
            log.info("【渠道推单】无匹配金额类型, 金额为 ： {}, 订单号为：{}", recordOrder.getOrderPrice(), recordOrder.getPlatformOrderNo());
            return false;
        }
        ShopUserInfo userInfo = shopUserMapper.selectById(shopPushMatch.orElse(pushMatch).getShopId());
        availableOrder.setShopUserId(userInfo.getId());
        availableOrder.setShopName(userInfo.getShopName());
        availableOrder.setShopPrice(availableOrder.getOrderPrice().multiply(BigDecimal.valueOf(userInfo.getRate())));
        availableOrder.setAisleId(aisleClassifyId);
        availableOrder.setCreateTime(new Date());
        //获取推送地址
        ShopMatchClassify matchClassify = shopMatchClassifyService.getById(userInfo.getMatchClassifyId());
        MqMessage mqMessage = new MqMessage();
        mqMessage.setQuerySite(userInfo.getQuerySite());
        mqMessage.setPlatformOrderNo(availableOrder.getPlatformOrderNo());
        mqMessage.setClassifyName(matchClassify.getName());
        mqMessage.setAppId(userInfo.getAppId());
        mqMessage.setAppKey(userInfo.getAppKey());

        PhoneOrderWaitPush phoneOrderWaitPush = new PhoneOrderWaitPush();
        phoneOrderWaitPush.setRate(userInfo.getRate())
                .setPushSite(userInfo.getPushSite())
                .setAppId(userInfo.getAppId())
                .setAppKey(userInfo.getAppKey())
                .setPhoneOrderAvailable(availableOrder)
                .setClassifyName(matchClassify.getName())
                .setShopCallbackSite(userInfo.getShopCallbackSite());
        //入库操作
        PhoneOrderRecord phoneOrderRecord = BeanUtils.copyPropertiesChaining(availableOrder, PhoneOrderRecord::new);
        //PhoneOrderAvailable做一次保存
        boolean saveByAvailableOrder = this.save(availableOrder);
        //PhoneOrderRecord做一次保存
        boolean saveByRecord = phoneOrderRecordService.save(phoneOrderRecord);
        //单日单个用户数据统计
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //key-->前缀 + 用户id + 运营商 + 时间
        String key = RedisConstant.CURRENT_PUSH_NUM + date + ":" + userInfo.getId() + ":" + availableOrder.getPhoneOperator() + ":" + availableOrder.getOrderPrice();
        intRedisTemplate.opsForValue().setIfAbsent(key, 0, 2, TimeUnit.DAYS);
        intRedisTemplate.opsForValue().increment(key);
        redisTemplate.opsForList().leftPush(RedisConstant.PUSH_ORDER_WAIT_PUSH, JSON.toJSONString(phoneOrderWaitPush));
        //发送延时队列   商户id和平台订单号  商户appId appkey  querySite查询地址
        if (!RechargeTypeEnum.SLOW.getCode().equals(rechargeType)) {
            rabbitMQService.sendDelayMessage(RabbitMQConstant.RABBIT_DELAY_EXCHANGE,
                    RabbitMQConstant.RABBIT_DELAY_ORDER_TIME_OUT_ROUTING_KEY, mqMessage, getRealOrderExpireTime(availableOrder.getOrderExpireTime()));
        }
        return saveByAvailableOrder && saveByRecord;
    }

    private static Integer getRealOrderExpireTime(Integer orderExpireTime) {
        String param = SystemConfig.getParam(ParamConstants.TIME_OUT_UPPER);
        Integer systemOrderExpireTime = Integer.valueOf(param);

        if (orderExpireTime > systemOrderExpireTime) {
            orderExpireTime = systemOrderExpireTime;
        }
        return orderExpireTime;
    }

}
