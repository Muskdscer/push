package com.push.service.platform.impl;

import com.push.dao.platform.PlatformUserDao;
import com.push.entity.platform.PlatformUserInfo;
import com.push.service.platform.PlatformLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: 登录service接口实现类
 * Create DateTime: 2020-03-26 11:18
 *
 * 

 */
@Service
public class PlatformLoginServiceImpl implements PlatformLoginService {

    @Resource
    private PlatformUserDao platformUserDao;

    @Override
    public PlatformUserInfo platformLogin(String username, String password) {
        return platformUserDao.selectPlatformUserByUserNameAndPassword(username, password);
    }

}
