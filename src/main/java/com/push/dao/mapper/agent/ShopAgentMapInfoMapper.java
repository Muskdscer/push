package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.ShopAgentMapVO;
import com.push.entity.agent.ShopAgentMapInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商户->代理商映射关系表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-04-26
 */
public interface ShopAgentMapInfoMapper extends BaseMapper<ShopAgentMapInfo> {

    Page<ShopAgentMapVO> getShopAgentMap(Page<ShopAgentMapVO> page, @Param("id") Long id );
}
