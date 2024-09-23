package com.push.service.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.AgentOperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.agent.ShopAgentLogOperate;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface ShopAgentLogOperateService extends IService<ShopAgentLogOperate> {

    IPage<OperateVO> listShopAgentOperateLog(AgentOperateBO bo);
}
