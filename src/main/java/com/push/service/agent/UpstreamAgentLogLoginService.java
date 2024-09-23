package com.push.service.agent;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.entity.agent.UpstreamAgentLogLogin;

/**
 * <p>
 * 上游代理商登录日志表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface UpstreamAgentLogLoginService extends IService<UpstreamAgentLogLogin> {

    Page<LoginLogVO> getUpstreamAgentLoginLog(LoginLogBO bo);
}
