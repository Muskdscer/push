package com.push.service.agent;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.agent.CpShopAgentBillInfo;

import java.util.List;

/**
 * <p>
 * 商户代理商账单事务表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface CpShopAgentBillInfoService extends IService<CpShopAgentBillInfo> {
    List<CpShopAgentBillInfo> findAllByState(Integer status);
}
