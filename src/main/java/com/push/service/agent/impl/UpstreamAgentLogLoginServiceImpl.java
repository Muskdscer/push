package com.push.service.agent.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.dao.mapper.agent.UpstreamAgentLogLoginMapper;
import com.push.entity.agent.UpstreamAgentLogLogin;
import com.push.service.agent.UpstreamAgentLogLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 上游代理商登录日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Service
public class UpstreamAgentLogLoginServiceImpl extends ServiceImpl<UpstreamAgentLogLoginMapper, UpstreamAgentLogLogin> implements UpstreamAgentLogLoginService {

    @Resource
    private UpstreamAgentLogLoginMapper upstreamAgentLogLoginMapper;

    @Override
    public Page<LoginLogVO> getUpstreamAgentLoginLog(LoginLogBO bo) {
        Page<LoginLogVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamAgentLogLoginMapper.getUpstreamAgentLoginLog(page, bo.getUserName(), bo.getLoginIp(), bo.getStartTime(), bo.getEndTime());
    }
}
