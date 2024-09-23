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
import com.push.dao.mapper.agent.ShopAgentBillInfoMapper;
import com.push.entity.agent.ShopAgentBillInfo;
import com.push.service.agent.ShopAgentBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商户代理商账单表 服务实现类
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Service
public class ShopAgentBillInfoServiceImpl extends ServiceImpl<ShopAgentBillInfoMapper, ShopAgentBillInfo> implements ShopAgentBillInfoService , IExcelExportServer {

    @Resource
    private ShopAgentBillInfoMapper shopAgentBillInfoMapper;

    @Override
    public ShopAgentBillInfo queryBillInfoByTradeNo(String tradeNo) {
        LambdaQueryWrapper<ShopAgentBillInfo> wrapper = Wrappers.lambdaQuery(ShopAgentBillInfo.class);
        wrapper.eq(ShopAgentBillInfo::getTradeNo, tradeNo);
        return shopAgentBillInfoMapper.selectOne(wrapper);
    }

    @Override
    public IPage<AgentBillInfoVO> listBillShopAgent(ListAgentBillBO bo) {
        IPage<AgentBillInfoVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return shopAgentBillInfoMapper.listBillShopAgent(page,bo);
    }

    @Override
    public List<ListBillAllTypeStatisticsVO> shopAgentBillStatistics(ListAgentBillBO bo) {
        return shopAgentBillInfoMapper.shopAgentBillStatistics(bo);
    }

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        ExportBillBO exportBillBO = (ExportBillBO) queryParams;
        int pageSize = 500;
        page = (page - 1) * pageSize;
        return new ArrayList<>(shopAgentBillInfoMapper.getExportField(exportBillBO, page, pageSize));
    }
}
