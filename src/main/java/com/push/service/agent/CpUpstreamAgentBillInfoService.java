package com.push.service.agent;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.agent.CpUpstreamAgentBillInfo;

import java.util.List;

/**
 * <p>
 * 上游代理商账单事务表 服务类
 * </p>
 *
 *

 * @since 2020-04-26
 */
public interface CpUpstreamAgentBillInfoService extends IService<CpUpstreamAgentBillInfo> {

    List<CpUpstreamAgentBillInfo> findAllByState(Integer status);
}
