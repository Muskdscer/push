package com.push.dao.mapper.agent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.bo.agent.ListAgentBillBO;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.agent.AgentBillInfoVO;
import com.push.common.webfacade.vo.platform.ExportShopAgentBillVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.entity.agent.ShopAgentBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商户代理商账单表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-04-26
 */
public interface ShopAgentBillInfoMapper extends BaseMapper<ShopAgentBillInfo> {

    IPage<AgentBillInfoVO> listBillShopAgent(@Param("page") IPage<AgentBillInfoVO> page, @Param("bo") ListAgentBillBO bo);

    List<ExportShopAgentBillVO> getExportField(@Param("bo") ExportBillBO bo, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    void saveBatch(@Param("shopAgentBillInfos") List<ShopAgentBillInfo> shopAgentBillInfos);

    List<ListBillAllTypeStatisticsVO> shopAgentBillStatistics(@Param("bo") ListAgentBillBO bo);
}
