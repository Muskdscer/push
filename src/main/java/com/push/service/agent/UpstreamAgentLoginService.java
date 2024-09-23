package com.push.service.agent;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.agent.UpstreamAgentUserInfo;

/**
 * Description:
 * Create DateTime: 2020/4/28 13:49
 *
 * 

 */
public interface UpstreamAgentLoginService extends IService<UpstreamAgentUserInfo> {
    UpstreamAgentUserInfo upstreamAgentLogin(String username, String password);

}
