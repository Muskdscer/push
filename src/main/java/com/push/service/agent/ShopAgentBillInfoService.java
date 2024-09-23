package com.push.service.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.agent.ListAgentBillBO;
import com.push.common.webfacade.vo.agent.AgentBillInfoVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.entity.agent.ShopAgentBillInfo;

import java.util.List;

/**
 * <p>
 * 商户代理商账单表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface ShopAgentBillInfoService extends IService<ShopAgentBillInfo> {

    ShopAgentBillInfo queryBillInfoByTradeNo(String tradeNo);

    IPage<AgentBillInfoVO> listBillShopAgent(ListAgentBillBO bo);

    List<ListBillAllTypeStatisticsVO> shopAgentBillStatistics(ListAgentBillBO bo);
}
