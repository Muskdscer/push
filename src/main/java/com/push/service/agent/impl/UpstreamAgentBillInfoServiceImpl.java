package com.push.service.agent.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.agent.ListAgentBillBO;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.vo.agent.AgentBillInfoVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.dao.mapper.agent.UpstreamAgentBillInfoMapper;
import com.push.entity.agent.UpstreamAgentBillInfo;
import com.push.service.agent.UpstreamAgentBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 上游代理商账单表 服务实现类
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Service
public class UpstreamAgentBillInfoServiceImpl extends ServiceImpl<UpstreamAgentBillInfoMapper, UpstreamAgentBillInfo> implements UpstreamAgentBillInfoService  , IExcelExportServer {

    @Resource
    private UpstreamAgentBillInfoMapper upstreamAgentBillInfoMapper;

    @Override
    public IPage<AgentBillInfoVO> listBillUpstreamAgent(ListAgentBillBO bo) {
        IPage<AgentBillInfoVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamAgentBillInfoMapper.listBillUpstreamAgent(page, bo);
    }
    public UpstreamAgentBillInfo queryBillInfoByTradeNo(String tradeNo) {
        LambdaQueryWrapper<UpstreamAgentBillInfo> wrapper = Wrappers.lambdaQuery(UpstreamAgentBillInfo.class);
        wrapper.eq(UpstreamAgentBillInfo::getTradeNo,tradeNo);
        return upstreamAgentBillInfoMapper.selectOne(wrapper);
    }

    @Override
    public List<ListBillAllTypeStatisticsVO> upstreamAgentBillStatistics(ListAgentBillBO bo) {
        return upstreamAgentBillInfoMapper.upstreamAgentBillStatistics(bo);
    }

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        ExportBillBO exportBillBO = (ExportBillBO) queryParams;
        int pageSize = 500;
        page = (page - 1) * pageSize;
        return new ArrayList<>(upstreamAgentBillInfoMapper.getExportField(exportBillBO, page, pageSize));
    }
}
