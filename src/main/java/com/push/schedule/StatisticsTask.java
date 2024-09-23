package com.push.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.common.constants.RedisConstant;
import com.push.common.enums.*;
import com.push.common.enums.statistics.StatisticsHandleTypeEnum;
import com.push.common.enums.statistics.StatisticsStatusEnum;
import com.push.common.enums.statistics.StatisticsUserTypeCodeEnum;
import com.push.common.webfacade.vo.platform.AlarmSystemVO;
import com.push.common.webfacade.vo.platform.StatisticShopCallbackVO;
import com.push.common.webfacade.vo.platform.StatisticUpstreamCallbackVO;
import com.push.common.webfacade.vo.platform.statistics.StatisticUpstreamCallbackFailVO;
import com.push.dao.mapper.agent.ShopAgentUserExtractionMapper;
import com.push.dao.mapper.agent.UpstreamAgentExtractionMapper;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.dao.mapper.platform.PlatformExtractionMapper;
import com.push.dao.mapper.shop.ShopUserExtractionMapper;
import com.push.dao.mapper.upstream.UpstreamExtractionMapper;
import com.push.dao.mapper.upstream.UpstreamUserRechargeMapper;
import com.push.entity.platform.*;
import com.push.service.platform.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 统计任务
 * Create DateTime: 2020-04-16 09:11
 *
 *

 */
@Slf4j
//@Component
public class StatisticsTask {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private PhoneOrderRecordService phoneOrderRecordService;
    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;
    @Resource
    private StatisticsBillService statisticsBillService;
    @Resource
    private PlatformExtractionMapper platformExtractionMapper;
    @Resource
    private UpstreamExtractionMapper upstreamExtractionMapper;
    @Resource
    private ShopUserExtractionMapper shopUserExtractionMapper;
    @Resource
    private UpstreamUserRechargeMapper upstreamUserRechargeMapper;
    @Resource
    private PhoneOrderFailRecordService phoneOrderFailRecordService;
    @Resource
    private ShopAgentUserExtractionMapper shopAgentUserExtractionMapper;
    @Resource
    private UpstreamAgentExtractionMapper upstreamAgentExtractionMapper;

    @Resource
    private PhoneOrderStatisticsService phoneOrderStatisticsService;

    @Resource
    private PhoneOrderSuccessProbabilityService phoneOrderSuccessProbabilityService;

    /**
     * 成功率
     */
    @Scheduled(cron = "0 0 1 1-31 * ?")
    public void orderSuccessProbabilityTask() {
        DateTime begin = DateUtil.yesterday();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(begin);
        Date end = com.push.common.utils.DateUtil.addOneDay(begin);
        List<Long> uIds = phoneOrderRecordService.list(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .groupBy(PhoneOrderRecord::getUpstreamUserId)
                .between(PhoneOrderRecord::getCreateTime, begin, end))
                .stream()
                .map(PhoneOrderRecord::getUpstreamUserId)
                .collect(Collectors.toList());
        List<PhoneOrderSuccessProbability> yesUpList = new ArrayList<>();
        List<PhoneOrderSuccessProbability> yesShopList = new ArrayList<>();
        uIds.forEach((id) -> {
            int count = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getUpstreamUserId, id).between(PhoneOrderRecord::getCreateTime, begin, end));
            int upSuccess = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getUpstreamUserId, id).eq(PhoneOrderRecord::getPlatformOrderStatus, 2).between(PhoneOrderRecord::getCreateTime, begin, end));
            PhoneOrderSuccessProbability probability = new PhoneOrderSuccessProbability();
            probability.setUserType(UserTypeCodeEnum.UPSTREAM_USER.getCode()).setRecordDate(currentDate).setUserId(id);
            if (count == 0) {
                probability.setSuccessProbability(0.0000);
            } else {
                probability.setSuccessProbability(Double.valueOf(new DecimalFormat("0.0000").format((double) upSuccess / (double) count)));
            }
            yesUpList.add(probability);
        });
        //商户成功率
        List<Long> sIds = phoneOrderRecordService.list(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .groupBy(PhoneOrderRecord::getShopUserId)
                .between(PhoneOrderRecord::getCreateTime, begin, end))
                .stream()
                .map(PhoneOrderRecord::getShopUserId)
                .collect(Collectors.toList());
        sIds.forEach((id) -> {
            int count = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getShopUserId, id).between(PhoneOrderRecord::getCreateTime, begin, end));
            int upSuccess = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class).eq(PhoneOrderRecord::getShopUserId, id).eq(PhoneOrderRecord::getPlatformOrderStatus, 2).between(PhoneOrderRecord::getCreateTime, begin, end));
            PhoneOrderSuccessProbability probability = new PhoneOrderSuccessProbability();
            probability.setUserType(UserTypeCodeEnum.SHOP_USER.getCode()).setRecordDate(currentDate).setUserId(id);
            if (count == 0) {
                probability.setSuccessProbability(0.0000);
            } else {
                probability.setSuccessProbability(Double.valueOf(new DecimalFormat("0.0000").format((double) upSuccess / (double) count)));
            }
            yesShopList.add(probability);
        });
        yesUpList.addAll(yesShopList);
        phoneOrderSuccessProbabilityService.saveBatch(yesUpList);
    }

    /**
     * 统计回调上游三次
     */


    /**
     * 订单统计
     */
    @Scheduled(cron = "0 0 1 1-31 * ?")
    public void orderStatisticsTask() {
        DateTime begin = DateUtil.yesterday();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(begin);
        Date end = com.push.common.utils.DateUtil.addOneDay(begin);
        List<PhoneOrderStatistics> statistics = new ArrayList<>();
        OperatorTypeEnum[] values = OperatorTypeEnum.values();
        for (OperatorTypeEnum value : values) {
            PhoneOrderStatistics phoneOrderStatistics = new PhoneOrderStatistics();
            int count = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                    .eq(PhoneOrderRecord::getPhoneOperator, value.getCode())
                    .between(PhoneOrderRecord::getCreateTime, begin, end));
            int success = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                    .eq(PhoneOrderRecord::getPhoneOperator, value.getCode())
                    .eq(PhoneOrderRecord::getPlatformOrderStatus, 2)
                    .between(PhoneOrderRecord::getCreateTime, begin, end));
            int fail = phoneOrderRecordService.count(Wrappers.lambdaQuery(PhoneOrderRecord.class)
                    .eq(PhoneOrderRecord::getPhoneOperator, value.getCode())
                    .and(wrapper->wrapper.eq(PhoneOrderRecord::getPlatformOrderStatus, -1)
                            .or()
                            .eq(PhoneOrderRecord::getPlatformOrderStatus, 3))
                    .between(PhoneOrderRecord::getCreateTime, begin, end));
            phoneOrderStatistics
                    .setOrderFailNum(fail)
                    .setOrderResultNum(count)
                    .setOrderSuccessNum(success)
                    .setStatisticsDate(currentDate)
                    .setPhoneOperator(value.getCode());
            statistics.add(phoneOrderStatistics);
        }
        phoneOrderStatisticsService.saveBatch(statistics);
    }

    /**
     * 首页统计商户未回调和渠道未回调以及渠道回调3次未成功
     */
    @Scheduled(fixedDelay = 60000)
    public void statisticsAlarm() {
        AlarmSystemVO alarmSystemVO = new AlarmSystemVO();
        List<StatisticShopCallbackVO> statisticShops = phoneOrderRecordService.getStatisticShop();
        List<StatisticUpstreamCallbackVO> statisticUpstreams = phoneOrderRecordService.getStatisticUpstream();
        List<StatisticUpstreamCallbackFailVO> statisticUpstreamCallBackFails = phoneOrderRecordService.getStatisticUpstreamCallBackFail();
        alarmSystemVO.setShopStatistics(statisticShops);
        alarmSystemVO.setUpstreamStatistics(statisticUpstreams);
        alarmSystemVO.setStatisticUpstreamCallBackFails(statisticUpstreamCallBackFails);
        if (statisticShops.size() != 0) {
            statisticShops.forEach(StatisticShopCallbackVO -> {
                if (StatisticShopCallbackVO.getWaitCallbackNo() > StatisticShopCallbackVO.getAlarmNumber()) {
                    StatisticShopCallbackVO.setAlarmFlag(AlarmFlagEnum.ALARMED.getCode());
                } else {
                    StatisticShopCallbackVO.setAlarmFlag(AlarmFlagEnum.UNALARM.getCode());
                }
            });
        }
        if (statisticUpstreams.size() != 0) {
            statisticUpstreams.forEach(StatisticUpstreamCallbackVO -> {
                if (StatisticUpstreamCallbackVO.getWaitCallbackNo() > StatisticUpstreamCallbackVO.getAlarmNumber()) {
                    StatisticUpstreamCallbackVO.setAlarmFlag(AlarmFlagEnum.ALARMED.getCode());
                } else {
                    StatisticUpstreamCallbackVO.setAlarmFlag(AlarmFlagEnum.UNALARM.getCode());
                }
            });
        }
        if (statisticUpstreams.size() != 0) {
            statisticUpstreamCallBackFails.forEach(statisticUpstreamCallbackFailVO -> {
                if (statisticUpstreamCallbackFailVO.getWaitCallbackNo() > statisticUpstreamCallbackFailVO.getAlarmNumber()) {
                    statisticUpstreamCallbackFailVO.setAlarmFlag(AlarmFlagEnum.ALARMED.getCode());
                } else {
                    statisticUpstreamCallbackFailVO.setAlarmFlag(AlarmFlagEnum.UNALARM.getCode());
                }
            });
        }
        stringRedisTemplate.opsForValue().set(RedisConstant.PUSH_ORDER_CALL_BACK_NUMBER_ALARM, JSON.toJSONString(alarmSystemVO));
    }


    //每天凌晨存一次
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void statisticsDeal() {
        Date date = new Date();
        Date yesterday = getYesterday(date);
        //当天时间的开始
        DateTime todayBegin = DateUtil.beginOfDay(yesterday);
        //当天时间的结束
        DateTime todayEnd = DateUtil.endOfDay(yesterday);
        StatisticsBill statisticsBill = new StatisticsBill();
        //记录时间
        Date recordTime = getRecordTime(date);
        //本日交易流水
        BigDecimal dealMoney = phoneOrderRecordMapper.getDealMoney(todayBegin, todayEnd, null);
        if (dealMoney != null) {
            statisticsBill.setSumMoney(dealMoney).setHandleType(StatisticsHandleTypeEnum.DEAL.getCode())
                    .setStatus(StatisticsStatusEnum.TOTAL_MONEY.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //本日交易成功金额
        BigDecimal dealSuccessMoney = phoneOrderRecordMapper.getDealMoney(todayBegin, todayEnd, OrderStatusCodeEnum.CONSUMPTION_SUCCESS.getCode());
        if (dealSuccessMoney != null) {
            statisticsBill.setSumMoney(dealSuccessMoney).setHandleType(StatisticsHandleTypeEnum.DEAL.getCode())
                    .setStatus(StatisticsStatusEnum.SUCCESS.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //本日交易失败金额
        BigDecimal dealFailMoney = phoneOrderRecordMapper.getDealFailMoney(todayBegin, todayEnd, OrderStatusCodeEnum.CONSUMPTION_FAIL.getCode(),
                OrderStatusCodeEnum.PUSH_FAILED.getCode());
        if (dealFailMoney != null) {
            statisticsBill.setSumMoney(dealFailMoney).setHandleType(StatisticsHandleTypeEnum.DEAL.getCode())
                    .setStatus(StatisticsStatusEnum.FAIL.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //平台本日提现金额
        BigDecimal platformExtractionMoney = platformExtractionMapper.getPlatformExtractionMoney(todayBegin, todayEnd, null);
        if (platformExtractionMoney != null) {
            statisticsBill.setSumMoney(platformExtractionMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.TOTAL_MONEY.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //平台本日提现成功金额
        BigDecimal platformExtractionSuccessMoney = platformExtractionMapper.getPlatformExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        if (platformExtractionSuccessMoney != null) {
            statisticsBill.setSumMoney(platformExtractionSuccessMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.SUCCESS.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //平台本日提现失败金额
        BigDecimal platformExtractionFailMoney = platformExtractionMapper.getPlatformExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        if (platformExtractionFailMoney != null) {
            statisticsBill.setSumMoney(platformExtractionFailMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.FAIL.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道本日提现金额
        BigDecimal upstreamExtractionMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(todayBegin, todayEnd, null);
        if (upstreamExtractionMoney != null) {
            statisticsBill.setSumMoney(upstreamExtractionMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.TOTAL_MONEY.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道本日提现成功金额
        BigDecimal upstreamExtractionSuccessMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        if (upstreamExtractionSuccessMoney != null) {
            statisticsBill.setSumMoney(upstreamExtractionSuccessMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.SUCCESS.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道本日提现失败金额
        BigDecimal upstreamExtractionFailMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        if (upstreamExtractionFailMoney != null) {
            statisticsBill.setSumMoney(upstreamExtractionFailMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.FAIL.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //商户本日提现金额
        BigDecimal shopExtractionMoney = shopUserExtractionMapper.getShopExtractionMoney(todayBegin, todayEnd, null);
        if (shopExtractionMoney != null) {
            statisticsBill.setSumMoney(shopExtractionMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.TOTAL_MONEY.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //商户本日提现成功金额
        BigDecimal shopExtractionSuccessMoney = shopUserExtractionMapper.getShopExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        if (shopExtractionSuccessMoney != null) {
            statisticsBill.setSumMoney(shopExtractionSuccessMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.SUCCESS.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //商户本日提现失败金额
        BigDecimal shopExtractionFailMoney = shopUserExtractionMapper.getShopExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        if (shopExtractionFailMoney != null) {
            statisticsBill.setSumMoney(shopExtractionFailMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.FAIL.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道本日充值金额
        BigDecimal upstreamRechargeMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(todayBegin, todayEnd, null);
        if (upstreamRechargeMoney != null) {
            statisticsBill.setSumMoney(upstreamRechargeMoney).setHandleType(StatisticsHandleTypeEnum.RECHARGE.getCode())
                    .setStatus(StatisticsStatusEnum.TOTAL_MONEY.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道本日充值成功金额
        BigDecimal upstreamRechargeSuccessMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(todayBegin, todayEnd, RechargeStatusEnum.SUCCESSS.getCode());
        if (upstreamRechargeSuccessMoney != null) {
            statisticsBill.setSumMoney(upstreamRechargeSuccessMoney).setHandleType(StatisticsHandleTypeEnum.RECHARGE.getCode())
                    .setStatus(StatisticsStatusEnum.SUCCESS.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道本日充值失败金额
        BigDecimal upstreamRechargeFailMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(todayBegin, todayEnd, RechargeStatusEnum.FAIL.getCode());
        if (upstreamRechargeFailMoney != null) {
            statisticsBill.setSumMoney(upstreamRechargeFailMoney).setHandleType(StatisticsHandleTypeEnum.RECHARGE.getCode())
                    .setStatus(StatisticsStatusEnum.FAIL.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //商户代理商本日提现金额
        BigDecimal shopAgentExtractionMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(todayBegin, todayEnd, null);
        if (shopAgentExtractionMoney != null) {
            statisticsBill.setSumMoney(shopAgentExtractionMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.TOTAL_MONEY.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //商户代理商本日提现成功金额
        BigDecimal shopAgentExtractionSuccessMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        if (shopAgentExtractionSuccessMoney != null) {
            statisticsBill.setSumMoney(shopAgentExtractionSuccessMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.SUCCESS.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //商户代理商本日提现失败金额
        BigDecimal shopAgentExtractionFailMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        if (shopAgentExtractionFailMoney != null) {
            statisticsBill.setSumMoney(shopAgentExtractionFailMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.FAIL.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道代理商本日提现金额
        BigDecimal upstreamAgentExtractionMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(todayBegin, todayEnd, null);
        if (upstreamAgentExtractionMoney != null) {
            statisticsBill.setSumMoney(upstreamAgentExtractionMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.TOTAL_MONEY.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道代理商本日提现成功金额
        BigDecimal upstreamAgentExtractionSuccessMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        if (upstreamAgentExtractionSuccessMoney != null) {
            statisticsBill.setSumMoney(upstreamAgentExtractionSuccessMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.SUCCESS.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
        //渠道代理商本日提现失败金额
        BigDecimal upstreamAgentExtractionFailMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        if (upstreamAgentExtractionFailMoney != null) {
            statisticsBill.setSumMoney(upstreamAgentExtractionFailMoney).setHandleType(StatisticsHandleTypeEnum.EXTRACTION.getCode())
                    .setStatus(StatisticsStatusEnum.FAIL.getCode())
                    .setRecordTime(recordTime)
                    .setUserType(StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
            statisticsBillService.save(statisticsBill);
        }
    }


    private Date getRecordTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private Date getYesterday(Date date) {
        Calendar calendarYesterday = Calendar.getInstance();
        calendarYesterday.setTime(date);
        calendarYesterday.set(Calendar.DATE, calendarYesterday.get(Calendar.DATE) - 1);
        return calendarYesterday.getTime();
    }

}
