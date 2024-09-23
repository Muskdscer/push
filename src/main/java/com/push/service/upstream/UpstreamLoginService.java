package com.push.service.upstream;

import com.push.entity.upstream.UpstreamUserInfo;

/**
 * Description: 上游渠道登录service接口
 * Create DateTime: 2020-03-27 09:08
 *
 *

 */
public interface UpstreamLoginService {

    /**
     * 上游渠道用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 上游渠道用户详情
     */
    UpstreamUserInfo upstreamLogin(String username, String password);

}
