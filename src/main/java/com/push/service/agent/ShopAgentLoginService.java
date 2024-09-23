package com.push.service.agent;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.agent.ShopAgentUserInfo;

/**
 * Description:
 * Create DateTime: 2020/4/28 13:47
 *
 * 

 */
public interface ShopAgentLoginService extends IService<ShopAgentUserInfo> {
    ShopAgentUserInfo shopAgentLogin(String username, String password);
}
