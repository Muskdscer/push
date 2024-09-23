package com.push.common.webfacade.vo.platform.statistics;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/23 10:04
 *
 *

 */
@Data
@Accessors(chain = true)
public class StatisticsUpstreamAgentExtractionMoneyVO {

    //渠道本日提现金额
    private BigDecimal todayUpstreamAgentExtractionMoney;
    //渠道本日提现成功金额
    private BigDecimal todayUpstreamAgentExtractionSuccessMoney;
    //渠道本日提现失败金额
    private BigDecimal todayUpstreamAgentExtractionFailMoney;
    //渠道昨日提现金额
    private BigDecimal yesterdayUpstreamAgentExtractionMoney;
    //渠道昨日提现成功金额
    private BigDecimal yesterdayUpstreamAgentExtractionSuccessMoney;
    //渠道昨日提现失败金额
    private BigDecimal yesterdayUpstreamAgentExtractionFailMoney;

    //渠道本月提现金额
    private BigDecimal thisMonthUpstreamAgentExtractionMoney;
    //渠道本月提现成功金额
    private BigDecimal thisMonthUpstreamAgentExtractionSuccessMoney;
    //渠道本月提现失败金额
    private BigDecimal thisMonthUpstreamAgentExtractionFailMoney;
    //渠道上月提现金额
    private BigDecimal lastMonthUpstreamAgentExtractionMoney;
    //渠道上月提现成功金额
    private BigDecimal lastMonthUpstreamAgentExtractionSuccessMoney;
    //渠道上月提现失败金额
    private BigDecimal lastMonthUpstreamAgentExtractionFailMoney;
}
