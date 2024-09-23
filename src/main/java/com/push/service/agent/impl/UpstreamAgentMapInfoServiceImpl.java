package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.webfacade.error.CommonException;
import com.push.common.webfacade.vo.platform.UpstreamAgentMapInfoVO;
import com.push.dao.mapper.agent.UpstreamAgentMapInfoMapper;
import com.push.entity.agent.UpstreamAgentMapInfo;
import com.push.service.agent.UpstreamAgentMapInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 渠道->代理商映射关系表 服务实现类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Slf4j
@Service
public class UpstreamAgentMapInfoServiceImpl extends ServiceImpl<UpstreamAgentMapInfoMapper, UpstreamAgentMapInfo> implements UpstreamAgentMapInfoService {

    @Resource
    private UpstreamAgentMapInfoMapper upstreamAgentMapInfoMapper;

    @Override
    public IPage<UpstreamAgentMapInfoVO> pageWithUpstream(Page<UpstreamAgentMapInfo> page, Long agentId, int code) {
        return upstreamAgentMapInfoMapper.pageWithUpstream(page, agentId, code);
    }

    @Override
    public Double getRateByUpstreamUserIdAndUpstreamAgentUserIdAndAisleId(Long upstreamUserId, Long upstreamAgentUserId, Long aisleId) {
        UpstreamAgentMapInfo upstreamAgentMapInfo = upstreamAgentMapInfoMapper.selectOne(Wrappers.lambdaQuery(UpstreamAgentMapInfo.class)
                .eq(UpstreamAgentMapInfo::getUpstreamId, upstreamUserId)
                .eq(UpstreamAgentMapInfo::getAgentId, upstreamAgentUserId)
                .eq(UpstreamAgentMapInfo::getAisleId, aisleId)
                .eq(UpstreamAgentMapInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));

        if (upstreamAgentMapInfo == null) {
            log.error("【渠道代理商映射】获取信息为空，渠道ID为:{}, 渠道代理商ID为：{}, 通道ID为：{}", upstreamUserId, upstreamAgentUserId, aisleId);
            throw new CommonException("渠道代理商映射信息不存在");
        }

        return upstreamAgentMapInfo.getRate();
    }
}
