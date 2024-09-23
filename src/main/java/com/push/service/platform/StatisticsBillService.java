package com.push.service.platform;


import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.vo.platform.statistics.*;
import com.push.entity.platform.StatisticsBill;

/**
 * <p>
 * 统计金额表 服务类
 * </p>
 *
 *

 * @since 2020-04-22
 */
public interface StatisticsBillService extends IService<StatisticsBill> {

    StatisticsDealMoneyVO statisticsDealMoney();

    StatisticsUpstreamExtractionMoneyVO statisticsUpstreamExtraction();

    StatisticsPlatformExtractionMoneyVO statisticsPlatformExtraction();

    StatisticsShopExtractionMoneyVO statisticsShopExtraction();

    StatisticsUpstreamRechargeMoneyVO statisticsUpstreamRecharge();

    StatisticsShopAgentExtractionMoneyVO statisticsShopAgentExtraction();

    StatisticsUpstreamAgentExtractionMoneyVO statisticsUpstreamAgentExtraction();
}
