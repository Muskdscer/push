package com.push.service.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.vo.platform.UpstreamAgentMapInfoVO;
import com.push.entity.agent.UpstreamAgentMapInfo;

/**
 * <p>
 * 渠道->代理商映射关系表 服务类
 * </p>
 *
 *

 * @since 2020-04-26
 */
public interface UpstreamAgentMapInfoService extends IService<UpstreamAgentMapInfo> {

    IPage<UpstreamAgentMapInfoVO> pageWithUpstream(Page<UpstreamAgentMapInfo> page, Long agentId, int code);

    /**
     * 根据渠道ID、渠道代理商ID、通道ID获取对应的费率
     *
     * @param upstreamUserId      渠道用户ID
     * @param upstreamAgentUserId 渠道代理商用户ID
     * @param aisleId             通道ID
     * @return 费率
     */
    Double getRateByUpstreamUserIdAndUpstreamAgentUserIdAndAisleId(Long upstreamUserId, Long upstreamAgentUserId, Long aisleId);
}
