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
public class StatisticsUpstreamExtractionMoneyVO {

    //渠道本日提现金额
    private BigDecimal todayUpstreamExtractionMoney;
    //渠道本日提现成功金额
    private BigDecimal todayUpstreamExtractionSuccessMoney;
    //渠道本日提现失败金额
    private BigDecimal todayUpstreamExtractionFailMoney;
    //渠道昨日提现金额
    private BigDecimal yesterdayUpstreamExtractionMoney;
    //渠道昨日提现成功金额
    private BigDecimal yesterdayUpstreamExtractionSuccessMoney;
    //渠道昨日提现失败金额
    private BigDecimal yesterdayUpstreamExtractionFailMoney;

    //渠道本月提现金额
    private BigDecimal thisMonthUpstreamExtractionMoney;
    //渠道本月提现成功金额
    private BigDecimal thisMonthUpstreamExtractionSuccessMoney;
    //渠道本月提现失败金额
    private BigDecimal thisMonthUpstreamExtractionFailMoney;
    //渠道上月提现金额
    private BigDecimal lastMonthUpstreamExtractionMoney;
    //渠道上月提现成功金额
    private BigDecimal lastMonthUpstreamExtractionSuccessMoney;
    //渠道上月提现失败金额
    private BigDecimal lastMonthUpstreamExtractionFailMoney;
}
