package com.push.service.platform.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.enums.ExtractionStatusEnum;
import com.push.common.enums.OrderStatusCodeEnum;
import com.push.common.enums.RechargeStatusEnum;
import com.push.common.enums.UpstreamCallbackEnum;
import com.push.common.enums.statistics.StatisticsHandleTypeEnum;
import com.push.common.enums.statistics.StatisticsStatusEnum;
import com.push.common.enums.statistics.StatisticsUserTypeCodeEnum;
import com.push.common.webfacade.vo.platform.statistics.*;
import com.push.dao.mapper.agent.ShopAgentUserExtractionMapper;
import com.push.dao.mapper.agent.UpstreamAgentExtractionMapper;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.dao.mapper.platform.PlatformExtractionMapper;
import com.push.dao.mapper.platform.StatisticsBillMapper;
import com.push.dao.mapper.shop.ShopUserExtractionMapper;
import com.push.dao.mapper.upstream.UpstreamExtractionMapper;
import com.push.dao.mapper.upstream.UpstreamUserRechargeMapper;
import com.push.entity.platform.StatisticsBill;
import com.push.service.platform.StatisticsBillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 统计金额表 服务实现类
 * </p>
 *
 *

 * @since 2020-04-22
 */
@Service
public class StatisticsBillServiceImpl extends ServiceImpl<StatisticsBillMapper, StatisticsBill> implements StatisticsBillService {
    private Date date = new Date();
    //当天时间的开始
    private DateTime todayBegin = DateUtil.beginOfDay(date);
    //当天时间的结束
    private DateTime todayEnd = DateUtil.endOfDay(date);
    private Date yesterday = getYesterday(date);
    //昨天时间的开始
    private DateTime yesterdayBegin = DateUtil.beginOfDay(yesterday);
    //昨天时间的结束
    private DateTime yesterdayEnd = DateUtil.endOfDay(yesterday);
    private Date thisMonthFirstDay = getThisMonthFirstDay(date);
    //本月月初
    private DateTime thisMonthFirstDayBegin = DateUtil.beginOfDay(thisMonthFirstDay);
    //上个月第一天的开始
    private DateTime lastMonthFirstDay = getLastMonthFirstDay(date);
    //上个月最后一天的结束
    private DateTime lastMonthEndDay = getLastMonthEndDay(date);

    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;
    @Resource
    private UpstreamUserRechargeMapper upstreamUserRechargeMapper;
    @Resource
    private UpstreamExtractionMapper upstreamExtractionMapper;
    @Resource
    private PlatformExtractionMapper platformExtractionMapper;
    @Resource
    private ShopUserExtractionMapper shopUserExtractionMapper;
    @Resource
    private ShopAgentUserExtractionMapper shopAgentUserExtractionMapper;
    @Resource
    private UpstreamAgentExtractionMapper upstreamAgentExtractionMapper;
    @Resource
    private StatisticsBillMapper statisticsBillMapper;


    @Override
    public StatisticsDealMoneyVO statisticsDealMoney() {
        StatisticsDealMoneyVO statisticsDealMoneyVO = new StatisticsDealMoneyVO();
        //本日交易流水
        BigDecimal todayDealMoney = phoneOrderRecordMapper.getDealMoney(todayBegin, todayEnd, null);
        //本日交易成功流水
        BigDecimal todayDealSuccessMoney = phoneOrderRecordMapper.getDealMoney(todayBegin, todayEnd, OrderStatusCodeEnum.CONSUMPTION_SUCCESS.getCode());
        //本日交易失败金额
        BigDecimal todayDealFailMoney = phoneOrderRecordMapper.getDealFailMoney(todayBegin, todayEnd, OrderStatusCodeEnum.CONSUMPTION_FAIL.getCode(),
                OrderStatusCodeEnum.PUSH_FAILED.getCode());
        //昨日交易流水
        BigDecimal yesterdayDealMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.DEAL.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), null);
        //昨日交易成功金额
        BigDecimal yesterdayDealSuccessMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.DEAL.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), null);
        //昨日交易失败金额
        BigDecimal yesterdayDealFailMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.DEAL.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), null);
        //本月交易流水
        BigDecimal thisMonthDealMoney = phoneOrderRecordMapper.getDealMoney(thisMonthFirstDayBegin, todayEnd, null);
        //本月交易成功金额
        BigDecimal thisMonthDealSuccessMoney = phoneOrderRecordMapper.getDealMoney(thisMonthFirstDayBegin, todayEnd, OrderStatusCodeEnum.CONSUMPTION_SUCCESS.getCode());
        //本月交易失败金额
        BigDecimal thisMonthDealFailMoney = phoneOrderRecordMapper.getDealFailMoney(thisMonthFirstDayBegin, todayEnd, OrderStatusCodeEnum.CONSUMPTION_FAIL.getCode(),
                OrderStatusCodeEnum.PUSH_FAILED.getCode());
        //上月交易流水
        BigDecimal lastMonthDealMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.DEAL.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), null);
        //上月交易成功金额
        BigDecimal lastMonthDealSuccessMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.DEAL.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), null);
        //上月交易失败金额
        BigDecimal lastMonthDealFailMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.DEAL.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), null);
        statisticsDealMoneyVO.setTodayDealMoney(todayDealMoney).setTodayDealSuccessMoney(todayDealSuccessMoney).setTodayDealFailMoney(todayDealFailMoney)
                .setYesterdayDealMoney(yesterdayDealMoney).setYesterdayDealSuccessMoney(yesterdayDealSuccessMoney).setYesterdayDealFailMoney(yesterdayDealFailMoney)
                .setThisMonthDealMoney(thisMonthDealMoney).setThisMonthDealSuccessMoney(thisMonthDealSuccessMoney).setThisMonthDealFailMoney(thisMonthDealFailMoney)
                .setLastMonthDealMoney(lastMonthDealMoney).setLastMonthDealSuccessMoney(lastMonthDealSuccessMoney).setLastMonthDealFailMoney(lastMonthDealFailMoney);
        return statisticsDealMoneyVO;
    }

    @Override
    public StatisticsUpstreamExtractionMoneyVO statisticsUpstreamExtraction() {
        StatisticsUpstreamExtractionMoneyVO statisticsUpstreamExtractionMoneyVO = new StatisticsUpstreamExtractionMoneyVO();
        //渠道本日提现金额
        BigDecimal todayUpstreamExtractionMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(todayBegin, todayEnd, null);
        //渠道本日提现成功金额
        BigDecimal todayUpstreamExtractionSuccessMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //渠道本日提现失败金额
        BigDecimal todayUpstreamExtractionFailMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //渠道昨日提现金额
        BigDecimal yesterdayUpstreamExtractionMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道昨日提现成功金额
        BigDecimal yesterdayUpstreamExtractionSuccessMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道昨日提现失败金额
        BigDecimal yesterdayUpstreamExtractionFailMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道本月提现金额
        BigDecimal thisMonthUpstreamExtractionMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(thisMonthFirstDayBegin, todayEnd, null);
        //渠道本月提现成功金额
        BigDecimal thisMonthUpstreamExtractionSuccessMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //渠道本月提现失败金额
        BigDecimal thisMonthUpstreamExtractionFailMoney = upstreamExtractionMapper.getUpstreamExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //渠道上月提现金额
        BigDecimal lastMonthUpstreamExtractionMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道上月提现成功金额
        BigDecimal lastMonthUpstreamExtractionSuccessMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道上月提现失败金额
        BigDecimal lastMonthUpstreamExtractionFailMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        statisticsUpstreamExtractionMoneyVO.setTodayUpstreamExtractionMoney(todayUpstreamExtractionMoney)
                .setTodayUpstreamExtractionSuccessMoney(todayUpstreamExtractionSuccessMoney)
                .setTodayUpstreamExtractionFailMoney(todayUpstreamExtractionFailMoney)
                .setYesterdayUpstreamExtractionMoney(yesterdayUpstreamExtractionMoney)
                .setYesterdayUpstreamExtractionSuccessMoney(yesterdayUpstreamExtractionSuccessMoney)
                .setYesterdayUpstreamExtractionFailMoney(yesterdayUpstreamExtractionFailMoney)
                .setThisMonthUpstreamExtractionMoney(thisMonthUpstreamExtractionMoney)
                .setThisMonthUpstreamExtractionSuccessMoney(thisMonthUpstreamExtractionSuccessMoney)
                .setThisMonthUpstreamExtractionFailMoney(thisMonthUpstreamExtractionFailMoney)
                .setLastMonthUpstreamExtractionMoney(lastMonthUpstreamExtractionMoney)
                .setLastMonthUpstreamExtractionSuccessMoney(lastMonthUpstreamExtractionSuccessMoney)
                .setLastMonthUpstreamExtractionFailMoney(lastMonthUpstreamExtractionFailMoney);
        return statisticsUpstreamExtractionMoneyVO;
    }

    @Override
    public StatisticsPlatformExtractionMoneyVO statisticsPlatformExtraction() {
        StatisticsPlatformExtractionMoneyVO statisticsPlatformExtractionMoneyVO = new StatisticsPlatformExtractionMoneyVO();
        //平台本日提现金额
        BigDecimal todayPlatformExtractionMoney = platformExtractionMapper.getPlatformExtractionMoney(todayBegin, todayEnd, null);
        //平台本日提现成功金额
        BigDecimal todayPlatformExtractionSuccessMoney = platformExtractionMapper.getPlatformExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //平台本日提现失败金额
        BigDecimal todayPlatformExtractionFailMoney = platformExtractionMapper.getPlatformExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //平台昨日提现金额
        BigDecimal yesterdayPlatformExtractionMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
        //平台昨日提现成功金额
        BigDecimal yesterdayPlatformExtractionSuccessMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
        //平台昨日提现失败金额
        BigDecimal yesterdayPlatformExtractionFailMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
        //平台本月提现金额
        BigDecimal thisMonthPlatformExtractionMoney = platformExtractionMapper.getPlatformExtractionMoney(thisMonthFirstDayBegin, todayEnd, null);
        //平台本月提现成功金额
        BigDecimal thisMonthPlatformExtractionSuccessMoney = platformExtractionMapper.getPlatformExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //平台本月提现失败金额
        BigDecimal thisMonthPlatformExtractionFailMoney = platformExtractionMapper.getPlatformExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //平台上月提现金额
        BigDecimal lastMonthPlatformExtractionMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
        //平台上月提现成功金额
        BigDecimal lastMonthPlatformExtractionSuccessMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
        //平台上月提现失败金额
        BigDecimal lastMonthPlatformExtractionFailMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.PLATFORM_USER.getCode());
        statisticsPlatformExtractionMoneyVO.setTodayPlatformExtractionMoney(todayPlatformExtractionMoney)
                .setTodayPlatformExtractionSuccessMoney(todayPlatformExtractionSuccessMoney)
                .setTodayPlatformExtractionFailMoney(todayPlatformExtractionFailMoney)
                .setYesterdayPlatformExtractionMoney(yesterdayPlatformExtractionMoney)
                .setYesterdayPlatformExtractionSuccessMoney(yesterdayPlatformExtractionSuccessMoney)
                .setYesterdayPlatformExtractionFailMoney(yesterdayPlatformExtractionFailMoney)
                .setThisMonthPlatformExtractionMoney(thisMonthPlatformExtractionMoney)
                .setThisMonthPlatformExtractionSuccessMoney(thisMonthPlatformExtractionSuccessMoney)
                .setThisMonthPlatformExtractionFailMoney(thisMonthPlatformExtractionFailMoney)
                .setLastMonthPlatformExtractionMoney(lastMonthPlatformExtractionMoney)
                .setLastMonthPlatformExtractionSuccessMoney(lastMonthPlatformExtractionSuccessMoney)
                .setLastMonthPlatformExtractionFailMoney(lastMonthPlatformExtractionFailMoney);
        return statisticsPlatformExtractionMoneyVO;
    }

    @Override
    public StatisticsShopExtractionMoneyVO statisticsShopExtraction() {
        StatisticsShopExtractionMoneyVO statisticsShopExtractionMoneyVO = new StatisticsShopExtractionMoneyVO();
        //商户本日提现金额
        BigDecimal todayShopExtractionMoney = shopUserExtractionMapper.getShopExtractionMoney(todayBegin, todayEnd, null);
        //商户本日提现成功金额
        BigDecimal todayShopExtractionSuccessMoney = shopUserExtractionMapper.getShopExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //商户本日提现失败金额
        BigDecimal todayShopExtractionFailMoney = shopUserExtractionMapper.getShopExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //商户昨日提现金额
        BigDecimal yesterdayShopExtractionMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
        //商户昨日提现成功金额
        BigDecimal yesterdayShopExtractionSuccessMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
        //商户昨日提现失败金额
        BigDecimal yesterdayShopExtractionFailMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
        //商户本月提现金额
        BigDecimal thisMonthShopExtractionMoney = shopUserExtractionMapper.getShopExtractionMoney(thisMonthFirstDayBegin, todayEnd, null);
        //商户本月提现成功金额
        BigDecimal thisMonthShopExtractionSuccessMoney = shopUserExtractionMapper.getShopExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //商户本月提现失败金额
        BigDecimal thisMonthShopExtractionFailMoney = shopUserExtractionMapper.getShopExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //商户上月提现金额
        BigDecimal lastMonthShopExtractionMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
        //商户上月提现成功金额
        BigDecimal lastMonthShopExtractionSuccessMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
        //商户上月提现失败金额
        BigDecimal lastMonthShopExtractionFailMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.SHOP_USER.getCode());
        statisticsShopExtractionMoneyVO.setTodayShopExtractionMoney(todayShopExtractionMoney)
                .setTodayShopExtractionSuccessMoney(todayShopExtractionSuccessMoney)
                .setTodayShopExtractionFailMoney(todayShopExtractionFailMoney)
                .setYesterdayShopExtractionMoney(yesterdayShopExtractionMoney)
                .setYesterdayShopExtractionSuccessMoney(yesterdayShopExtractionSuccessMoney)
                .setYesterdayShopExtractionFailMoney(yesterdayShopExtractionFailMoney)
                .setThisMonthShopExtractionMoney(thisMonthShopExtractionMoney)
                .setThisMonthShopExtractionSuccessMoney(thisMonthShopExtractionSuccessMoney)
                .setThisMonthShopExtractionFailMoney(thisMonthShopExtractionFailMoney)
                .setLastMonthShopExtractionMoney(lastMonthShopExtractionMoney)
                .setLastMonthShopExtractionSuccessMoney(lastMonthShopExtractionSuccessMoney)
                .setLastMonthShopExtractionFailMoney(lastMonthShopExtractionFailMoney);
        return statisticsShopExtractionMoneyVO;
    }

    @Override
    public StatisticsUpstreamRechargeMoneyVO statisticsUpstreamRecharge() {
        StatisticsUpstreamRechargeMoneyVO statisticsUpstreamRechargeMoneyVO = new StatisticsUpstreamRechargeMoneyVO();
        //渠道本日充值金额
        BigDecimal todayUpstreamRechargeMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(todayBegin, todayEnd, null);
        //渠道本日充值成功金额
        BigDecimal todayUpstreamRechargeSuccessMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(todayBegin, todayEnd, RechargeStatusEnum.SUCCESSS.getCode());
        //渠道本日充值失败金额
        BigDecimal todayUpstreamRechargeFailMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(todayBegin, todayEnd, RechargeStatusEnum.FAIL.getCode());
        //渠道昨日充值金额
        BigDecimal yesterdayUpstreamRechargeMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.RECHARGE.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道昨日充值成功金额
        BigDecimal yesterdayUpstreamRechargeSuccessMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.RECHARGE.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道昨日充值失败金额
        BigDecimal yesterdayUpstreamRechargeFailMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.RECHARGE.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道本月充值金额
        BigDecimal thisMonthUpstreamRechargeMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(thisMonthFirstDayBegin, todayEnd, null);
        //渠道本月充值成功金额
        BigDecimal thisMonthUpstreamRechargeSuccessMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(thisMonthFirstDayBegin, todayEnd, RechargeStatusEnum.SUCCESSS.getCode());
        //渠道本月充值失败金额
        BigDecimal thisMonthUpstreamRechargeFailMoney = upstreamUserRechargeMapper.getUpstreamRechargeMoney(thisMonthFirstDayBegin, todayEnd, RechargeStatusEnum.FAIL.getCode());
        //渠道上月充值金额
        BigDecimal lastMonthUpstreamRechargeMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.RECHARGE.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道上月充值成功金额
        BigDecimal lastMonthUpstreamRechargeSuccessMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.RECHARGE.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        //渠道上月充值失败金额
        BigDecimal lastMonthUpstreamRechargeFailMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.RECHARGE.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_USER.getCode());
        statisticsUpstreamRechargeMoneyVO.setTodayUpstreamRechargeMoney(todayUpstreamRechargeMoney)
                .setTodayUpstreamRechargeSuccessMoney(todayUpstreamRechargeSuccessMoney)
                .setTodayUpstreamRechargeFailMoney(todayUpstreamRechargeFailMoney)
                .setYesterdayUpstreamRechargeMoney(yesterdayUpstreamRechargeMoney)
                .setYesterdayUpstreamRechargeSuccessMoney(yesterdayUpstreamRechargeSuccessMoney)
                .setYesterdayUpstreamRechargeFailMoney(yesterdayUpstreamRechargeFailMoney)
                .setThisMonthUpstreamRechargeMoney(thisMonthUpstreamRechargeMoney)
                .setThisMonthUpstreamRechargeSuccessMoney(thisMonthUpstreamRechargeSuccessMoney)
                .setThisMonthUpstreamRechargeFailMoney(thisMonthUpstreamRechargeFailMoney)
                .setLastMonthUpstreamRechargeMoney(lastMonthUpstreamRechargeMoney)
                .setLastMonthUpstreamRechargeSuccessMoney(lastMonthUpstreamRechargeSuccessMoney)
                .setLastMonthUpstreamRechargeFailMoney(lastMonthUpstreamRechargeFailMoney);
        return statisticsUpstreamRechargeMoneyVO;
    }

    @Override
    public StatisticsShopAgentExtractionMoneyVO statisticsShopAgentExtraction() {
        StatisticsShopAgentExtractionMoneyVO statisticsShopAgentExtractionMoneyVO = new StatisticsShopAgentExtractionMoneyVO();
        //商户代理商本日提现金额
        BigDecimal todayShopAgentExtractionMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(todayBegin, todayEnd, null);
        //商户代理商本日提现成功金额
        BigDecimal todayShopAgentExtractionSuccessMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //商户代理商本日提现失败金额
        BigDecimal todayShopAgentExtractionFailMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //商户代理商昨日提现金额
        BigDecimal yesterdayShopAgentExtractionMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
        //商户代理商昨日提现成功金额
        BigDecimal yesterdayShopAgentExtractionSuccessMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
        //商户代理商昨日提现失败金额
        BigDecimal yesterdayShopAgentExtractionFailMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
        //商户代理商本月提现金额
        BigDecimal thisMonthShopAgentExtractionMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(thisMonthFirstDayBegin, todayEnd, null);
        //商户代理商本月提现成功金额
        BigDecimal thisMonthShopAgentExtractionSuccessMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //商户代理商本月提现失败金额
        BigDecimal thisMonthShopAgentExtractionFailMoney = shopAgentUserExtractionMapper.getShopAgentExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //商户代理商上月提现金额
        BigDecimal lastMonthShopAgentExtractionMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
        //商户代理商上月提现成功金额
        BigDecimal lastMonthShopAgentExtractionSuccessMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
        //商户代理商上月提现失败金额
        BigDecimal lastMonthShopAgentExtractionFailMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.SHOP_AGENT_USER.getCode());
        statisticsShopAgentExtractionMoneyVO.setTodayShopAgentExtractionMoney(todayShopAgentExtractionMoney)
                .setTodayShopAgentExtractionSuccessMoney(todayShopAgentExtractionSuccessMoney)
                .setTodayShopAgentExtractionFailMoney(todayShopAgentExtractionFailMoney)
                .setYesterdayShopAgentExtractionMoney(yesterdayShopAgentExtractionMoney)
                .setYesterdayShopAgentExtractionSuccessMoney(yesterdayShopAgentExtractionSuccessMoney)
                .setYesterdayShopAgentExtractionFailMoney(yesterdayShopAgentExtractionFailMoney)
                .setThisMonthShopAgentExtractionMoney(thisMonthShopAgentExtractionMoney)
                .setThisMonthShopAgentExtractionSuccessMoney(thisMonthShopAgentExtractionSuccessMoney)
                .setThisMonthShopAgentExtractionFailMoney(thisMonthShopAgentExtractionFailMoney)
                .setLastMonthShopAgentExtractionMoney(lastMonthShopAgentExtractionMoney)
                .setLastMonthShopAgentExtractionSuccessMoney(lastMonthShopAgentExtractionSuccessMoney)
                .setLastMonthShopAgentExtractionFailMoney(lastMonthShopAgentExtractionFailMoney);
        return statisticsShopAgentExtractionMoneyVO;
    }

    @Override
    public StatisticsUpstreamAgentExtractionMoneyVO statisticsUpstreamAgentExtraction() {
        StatisticsUpstreamAgentExtractionMoneyVO statisticsUpstreamAgentExtractionMoneyVO = new StatisticsUpstreamAgentExtractionMoneyVO();
        //渠道代理商本日提现金额
        BigDecimal todayUpstreamAgentExtractionMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(todayBegin, todayEnd, null);
        //渠道代理商本日提现成功金额
        BigDecimal todayUpstreamAgentExtractionSuccessMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //渠道代理商本日提现失败金额
        BigDecimal todayUpstreamAgentExtractionFailMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(todayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //渠道代理商昨日提现金额
        BigDecimal yesterdayUpstreamAgentExtractionMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
        //渠道代理商昨日提现成功金额
        BigDecimal yesterdayUpstreamAgentExtractionSuccessMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
        //渠道代理商昨日提现失败金额
        BigDecimal yesterdayUpstreamAgentExtractionFailMoney = statisticsBillMapper.getDealMoney(yesterdayBegin, yesterdayEnd, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
        //渠道代理商本月提现金额
        BigDecimal thisMonthUpstreamAgentExtractionMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(thisMonthFirstDayBegin, todayEnd, null);
        //渠道代理商本月提现成功金额
        BigDecimal thisMonthUpstreamAgentExtractionSuccessMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.SUCCESSS.getCode());
        //渠道代理商本月提现失败金额
        BigDecimal thisMonthUpstreamAgentExtractionFailMoney = upstreamAgentExtractionMapper.getUpstreamAgentExtractionMoney(thisMonthFirstDayBegin, todayEnd, ExtractionStatusEnum.FAIL.getCode());
        //渠道代理商上月提现金额
        BigDecimal lastMonthUpstreamAgentExtractionMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.TOTAL_MONEY.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
        //渠道代理商上月提现成功金额
        BigDecimal lastMonthUpstreamAgentExtractionSuccessMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.SUCCESS.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
        //渠道代理商上月提现失败金额
        BigDecimal lastMonthUpstreamAgentExtractionFailMoney = statisticsBillMapper.getDealMoney(lastMonthFirstDay, lastMonthEndDay, StatisticsHandleTypeEnum.EXTRACTION.getCode()
                , StatisticsStatusEnum.FAIL.getCode(), StatisticsUserTypeCodeEnum.UPSTREAM_AGENT_USER.getCode());
        statisticsUpstreamAgentExtractionMoneyVO.setTodayUpstreamAgentExtractionMoney(todayUpstreamAgentExtractionMoney)
                .setTodayUpstreamAgentExtractionSuccessMoney(todayUpstreamAgentExtractionSuccessMoney)
                .setTodayUpstreamAgentExtractionFailMoney(todayUpstreamAgentExtractionFailMoney)
                .setYesterdayUpstreamAgentExtractionMoney(yesterdayUpstreamAgentExtractionMoney)
                .setYesterdayUpstreamAgentExtractionSuccessMoney(yesterdayUpstreamAgentExtractionSuccessMoney)
                .setYesterdayUpstreamAgentExtractionFailMoney(yesterdayUpstreamAgentExtractionFailMoney)
                .setThisMonthUpstreamAgentExtractionMoney(thisMonthUpstreamAgentExtractionMoney)
                .setThisMonthUpstreamAgentExtractionSuccessMoney(thisMonthUpstreamAgentExtractionSuccessMoney)
                .setThisMonthUpstreamAgentExtractionFailMoney(thisMonthUpstreamAgentExtractionFailMoney)
                .setLastMonthUpstreamAgentExtractionMoney(lastMonthUpstreamAgentExtractionMoney)
                .setLastMonthUpstreamAgentExtractionSuccessMoney(lastMonthUpstreamAgentExtractionSuccessMoney)
                .setLastMonthUpstreamAgentExtractionFailMoney(lastMonthUpstreamAgentExtractionFailMoney);
        return statisticsUpstreamAgentExtractionMoneyVO;
    }


    private DateTime getLastMonthEndDay(Date date) {
        Calendar calendarMonth = Calendar.getInstance();
        calendarMonth.setTime(new Date());
        calendarMonth.set(Calendar.MONTH, calendarMonth.get(Calendar.MONTH));
        calendarMonth.set(Calendar.DATE, 0);
        Date month = calendarMonth.getTime();
        return DateUtil.endOfDay(month);
    }

    private DateTime getLastMonthFirstDay(Date date) {
        Calendar calendarMonth = Calendar.getInstance();
        calendarMonth.setTime(date);
        calendarMonth.set(Calendar.MONTH, calendarMonth.get(Calendar.MONTH) - 1);
        calendarMonth.set(Calendar.DATE, 1);
        Date month = calendarMonth.getTime();
        return DateUtil.beginOfDay(month);
    }

    private Date getThisMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    private Date getYesterday(Date date) {
        Calendar calendarYesterday = Calendar.getInstance();
        calendarYesterday.setTime(date);
        calendarYesterday.set(Calendar.DATE, calendarYesterday.get(Calendar.DATE) - 1);
        return calendarYesterday.getTime();
    }


}
