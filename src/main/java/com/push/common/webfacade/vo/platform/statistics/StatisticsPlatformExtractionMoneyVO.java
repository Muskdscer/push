package com.push.common.webfacade.vo.platform.statistics;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/23 10:45
 *
 *

 */
@Data
@Accessors(chain = true)
public class StatisticsPlatformExtractionMoneyVO {

    //平台本日提现金额
    private BigDecimal todayPlatformExtractionMoney;
    //平台本日提现成功金额
    private BigDecimal todayPlatformExtractionSuccessMoney;
    //平台本日提现失败金额
    private BigDecimal todayPlatformExtractionFailMoney;
    //平台昨日提现金额
    private BigDecimal yesterdayPlatformExtractionMoney;
    //平台昨日提现成功金额
    private BigDecimal yesterdayPlatformExtractionSuccessMoney;
    //平台昨日提现失败金额
    private BigDecimal yesterdayPlatformExtractionFailMoney;

    //平台本月提现金额
    private BigDecimal thisMonthPlatformExtractionMoney;
    //平台本月提现成功金额
    private BigDecimal thisMonthPlatformExtractionSuccessMoney;
    //平台本月提现失败金额
    private BigDecimal thisMonthPlatformExtractionFailMoney;
    //平台上月提现金额
    private BigDecimal lastMonthPlatformExtractionMoney;
    //平台上月提现成功金额
    private BigDecimal lastMonthPlatformExtractionSuccessMoney;
    //平台上月提现失败金额
    private BigDecimal lastMonthPlatformExtractionFailMoney;
}
