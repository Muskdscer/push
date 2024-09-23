package com.push.service.upstream.impl;

import com.push.dao.upstream.UpstreamUserDao;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: 上游渠道登录service接口实现类
 * Create DateTime: 2020-03-27 09:08
 *
 * 

 */
@Service
public class UpstreamLoginServiceImpl implements UpstreamLoginService {

    @Resource
    private UpstreamUserDao upstreamUserDao;

    @Override
    public UpstreamUserInfo upstreamLogin(String username, String password) {
        return upstreamUserDao.selectUpstreamUserByUserNameAndPassword(username, password);
    }

}
