package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.UpstreamAgentMapInfoVO;
import com.push.entity.agent.UpstreamAgentMapInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 渠道->代理商映射关系表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface UpstreamAgentMapInfoMapper extends BaseMapper<UpstreamAgentMapInfo> {

    IPage<UpstreamAgentMapInfoVO> pageWithUpstream(@Param("page") Page<UpstreamAgentMapInfo> page, @Param("agentId") Long agentId, @Param("code") int code);
}
