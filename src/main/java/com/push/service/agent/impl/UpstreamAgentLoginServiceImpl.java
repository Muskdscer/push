package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.enums.DeleteFlagEnum;
import com.push.dao.mapper.agent.UpstreamAgentUserInfoMapper;
import com.push.entity.agent.UpstreamAgentUserInfo;
import com.push.service.agent.UpstreamAgentLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020/4/28 13:49
 *
 * 

 */
@Service
public class UpstreamAgentLoginServiceImpl extends ServiceImpl<UpstreamAgentUserInfoMapper,UpstreamAgentUserInfo> implements UpstreamAgentLoginService {

    @Resource
    private UpstreamAgentUserInfoMapper upstreamAgentUserInfoMapper;

    @Override
    public UpstreamAgentUserInfo upstreamAgentLogin(String username, String password) {
        return upstreamAgentUserInfoMapper.selectOne(Wrappers.lambdaQuery(UpstreamAgentUserInfo.class)
                .eq(UpstreamAgentUserInfo::getUserName,username)
                .eq(UpstreamAgentUserInfo::getPassword,password)
                .eq(UpstreamAgentUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
    }
}
