package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.AgentOperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.dao.mapper.agent.UpstreamAgentLogOperateMapper;
import com.push.entity.agent.UpstreamAgentLogOperate;
import com.push.service.agent.UpstreamAgentLogOperateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 上游代理商操作日志表 服务实现类
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Service
public class UpstreamAgentLogOperateServiceImpl extends ServiceImpl<UpstreamAgentLogOperateMapper, UpstreamAgentLogOperate> implements UpstreamAgentLogOperateService {

    @Resource
    private UpstreamAgentLogOperateMapper upstreamAgentLogOperateMapper;

    @Override
    public IPage<OperateVO> listShopAgentOperateLog(AgentOperateBO bo) {
        Page<Object> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamAgentLogOperateMapper.listShopAgentOperateLog(page, bo.getUsername(), bo.getStartTime(), bo.getEndTime());
    }
}
