package com.push.common.webfacade.vo.platform.statistics;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/4/22 9:40
 *
 *

 */
@Data
@Accessors(chain = true)
public class StatisticsDealVO {
    //统计交易金额
    private StatisticsDealMoneyVO statisticsDealMoneyVO;
    //统计渠道提现金额
    private StatisticsUpstreamExtractionMoneyVO statisticsUpstreamExtractionMoneyVO;
    //统计平台提现金额
    private StatisticsPlatformExtractionMoneyVO platformExtractionMoneyVO;
    //统计商户提金额
    private StatisticsShopExtractionMoneyVO statisticsShopExtractionMoneyVO;
    //统计渠道充值金额
    private StatisticsUpstreamRechargeMoneyVO statisticsUpstreamRechargeMoneyVO;
    //统计商户代理商提现金额
    private StatisticsShopAgentExtractionMoneyVO statisticsShopAgentExtractionMoneyVO;
    //统计渠道代理商提现金额
    private StatisticsUpstreamAgentExtractionMoneyVO statisticsUpstreamAgentExtractionMoneyVO;
}
