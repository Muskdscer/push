package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.bo.agent.ListAgentBillBO;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.agent.AgentBillInfoVO;
import com.push.common.webfacade.vo.platform.ExportUpstreamAgentBillVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.entity.agent.UpstreamAgentBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 上游代理商账单表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface UpstreamAgentBillInfoMapper extends BaseMapper<UpstreamAgentBillInfo> {

    IPage<AgentBillInfoVO> listBillUpstreamAgent(@Param("page") IPage<AgentBillInfoVO> page, @Param("bo") ListAgentBillBO bo);

    List<ExportUpstreamAgentBillVO> getExportField(@Param("bo") ExportBillBO bo, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    void saveBatch(@Param("upstreamAgentBillInfos") List<UpstreamAgentBillInfo> upstreamAgentBillInfos);

    List<ListBillAllTypeStatisticsVO> upstreamAgentBillStatistics(@Param("bo") ListAgentBillBO bo);
}
