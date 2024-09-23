package com.push.common.webfacade.vo.platform.statistics;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/23 11:04
 *
 *

 */
@Data
@Accessors(chain = true)
public class StatisticsShopExtractionMoneyVO {

    //商户本日提现金额
    private BigDecimal todayShopExtractionMoney;
    //商户本日提现成功金额
    private BigDecimal todayShopExtractionSuccessMoney;
    //商户本日提现失败金额
    private BigDecimal todayShopExtractionFailMoney;
    //商户昨日提现金额
    private BigDecimal yesterdayShopExtractionMoney;
    //商户昨日提现成功金额
    private BigDecimal yesterdayShopExtractionSuccessMoney;
    //商户昨日提现失败金额
    private BigDecimal yesterdayShopExtractionFailMoney;

    //商户本月提现金额
    private BigDecimal thisMonthShopExtractionMoney;
    //商户本月提现成功金额
    private BigDecimal thisMonthShopExtractionSuccessMoney;
    //商户本月提现失败金额
    private BigDecimal thisMonthShopExtractionFailMoney;
    //商户上月提现金额
    private BigDecimal lastMonthShopExtractionMoney;
    //商户上月提现成功金额
    private BigDecimal lastMonthShopExtractionSuccessMoney;
    //商户上月提现失败金额
    private BigDecimal lastMonthShopExtractionFailMoney;
}
