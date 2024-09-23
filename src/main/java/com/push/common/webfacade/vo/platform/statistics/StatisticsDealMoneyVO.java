package com.push.common.webfacade.vo.platform.statistics;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/22 18:55
 *
 *

 */
@Data
@Accessors(chain = true)
public class StatisticsDealMoneyVO {
    //本日交易流水
    private BigDecimal todayDealMoney;
    //本日交易成功金额
    private BigDecimal todayDealSuccessMoney;
    //本日交易失败金额
    private BigDecimal todayDealFailMoney;
    //昨日交易流水
    private BigDecimal yesterdayDealMoney;
    //昨日交易成功金额
    private BigDecimal yesterdayDealSuccessMoney;
    //昨日交易失败金额
    private BigDecimal yesterdayDealFailMoney;

    //本月交易流水
    private BigDecimal thisMonthDealMoney;
    //本月交易成功金额
    private BigDecimal thisMonthDealSuccessMoney;
    //本月交易失败金额
    private BigDecimal thisMonthDealFailMoney;
    //上月交易流水
    private BigDecimal lastMonthDealMoney;
    //上月交易成功金额
    private BigDecimal lastMonthDealSuccessMoney;
    //上月交易失败金额
    private BigDecimal lastMonthDealFailMoney;

}
