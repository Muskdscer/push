package com.push.service.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.AgentOperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.agent.UpstreamAgentLogOperate;

/**
 * <p>
 * 上游代理商操作日志表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface UpstreamAgentLogOperateService extends IService<UpstreamAgentLogOperate> {

    IPage<OperateVO> listShopAgentOperateLog(AgentOperateBO bo);
}
