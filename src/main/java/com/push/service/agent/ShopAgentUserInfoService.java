package com.push.service.agent;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.vo.platform.ShopAgentMapVO;
import com.push.entity.agent.ShopAgentUserInfo;

/**
 * <p>
 * 商户代理商信息表 服务类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface ShopAgentUserInfoService extends IService<ShopAgentUserInfo> {

    Page<ShopAgentMapVO> getShopAgentMap(Page<ShopAgentMapVO> page, Long id);
}
