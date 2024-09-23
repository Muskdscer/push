package com.push.common.webfacade.vo.platform.statistics;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/23 11:19
 *
 *

 */
@Data
@Accessors(chain = true)
public class StatisticsUpstreamRechargeMoneyVO {

    //渠道本日充值金额
    private BigDecimal todayUpstreamRechargeMoney;
    //渠道本日充值成功金额
    private BigDecimal todayUpstreamRechargeSuccessMoney;
    //渠道本日充值失败金额
    private BigDecimal todayUpstreamRechargeFailMoney;
    //渠道昨日充值金额
    private BigDecimal yesterdayUpstreamRechargeMoney;
    //渠道昨日充值成功金额
    private BigDecimal yesterdayUpstreamRechargeSuccessMoney;
    //渠道昨日充值失败金额
    private BigDecimal yesterdayUpstreamRechargeFailMoney;

    //渠道本月充值金额
    private BigDecimal thisMonthUpstreamRechargeMoney;
    //渠道本月充值成功金额
    private BigDecimal thisMonthUpstreamRechargeSuccessMoney;
    //渠道本月充值失败金额
    private BigDecimal thisMonthUpstreamRechargeFailMoney;
    //渠道上月充值金额
    private BigDecimal lastMonthUpstreamRechargeMoney;
    //渠道上月充值成功金额
    private BigDecimal lastMonthUpstreamRechargeSuccessMoney;
    //渠道上月充值失败金额
    private BigDecimal lastMonthUpstreamRechargeFailMoney;
}
