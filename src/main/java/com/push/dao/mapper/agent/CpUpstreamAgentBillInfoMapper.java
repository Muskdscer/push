package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.agent.CpUpstreamAgentBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 上游代理商账单事务表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface CpUpstreamAgentBillInfoMapper extends BaseMapper<CpUpstreamAgentBillInfo> {

    void saveBatch(@Param("cpUpstreamAgentBillInfos") List<CpUpstreamAgentBillInfo> cpUpstreamAgentBillInfos);
}
