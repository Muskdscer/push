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
public class StatisticsShopAgentExtractionMoneyVO {

    //商户本日提现金额
    private BigDecimal todayShopAgentExtractionMoney;
    //商户本日提现成功金额
    private BigDecimal todayShopAgentExtractionSuccessMoney;
    //商户本日提现失败金额
    private BigDecimal todayShopAgentExtractionFailMoney;
    //商户昨日提现金额
    private BigDecimal yesterdayShopAgentExtractionMoney;
    //商户昨日提现成功金额
    private BigDecimal yesterdayShopAgentExtractionSuccessMoney;
    //商户昨日提现失败金额
    private BigDecimal yesterdayShopAgentExtractionFailMoney;

    //商户本月提现金额
    private BigDecimal thisMonthShopAgentExtractionMoney;
    //商户本月提现成功金额
    private BigDecimal thisMonthShopAgentExtractionSuccessMoney;
    //商户本月提现失败金额
    private BigDecimal thisMonthShopAgentExtractionFailMoney;
    //商户上月提现金额
    private BigDecimal lastMonthShopAgentExtractionMoney;
    //商户上月提现成功金额
    private BigDecimal lastMonthShopAgentExtractionSuccessMoney;
    //商户上月提现失败金额
    private BigDecimal lastMonthShopAgentExtractionFailMoney;
}
