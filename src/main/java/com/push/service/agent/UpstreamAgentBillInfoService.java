package com.push.service.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.agent.ListAgentBillBO;
import com.push.common.webfacade.vo.agent.AgentBillInfoVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.entity.agent.UpstreamAgentBillInfo;

import java.util.List;

/**
 * <p>
 * 上游代理商账单表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface UpstreamAgentBillInfoService extends IService<UpstreamAgentBillInfo> {


    IPage<AgentBillInfoVO> listBillUpstreamAgent(ListAgentBillBO bo);

    UpstreamAgentBillInfo queryBillInfoByTradeNo(String tradeNo);

    List<ListBillAllTypeStatisticsVO> upstreamAgentBillStatistics(ListAgentBillBO bo);
}
