package com.push.service.platform;

import com.push.entity.platform.PlatformUserInfo;

/**
 * Description: 登录service接口
 * Create DateTime: 2020-03-26 11:18
 *
 *

 */
public interface PlatformLoginService {

    /**
     * 平台用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 平台用户详情
     */
    PlatformUserInfo platformLogin(String username, String password);

}
