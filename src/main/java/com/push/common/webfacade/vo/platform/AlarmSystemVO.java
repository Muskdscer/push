package com.push.common.webfacade.vo.platform;

import com.push.common.webfacade.vo.platform.statistics.StatisticUpstreamCallbackFailVO;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/4/16 10:23
 *
 * 

 */
@Data
public class AlarmSystemVO {
    //商户统计集合
    private List<StatisticShopCallbackVO> shopStatistics;
    //上游统计集合
    private List<StatisticUpstreamCallbackVO> upstreamStatistics;
    //回调上游三次未成功
    private List<StatisticUpstreamCallbackFailVO> statisticUpstreamCallBackFails;

}
