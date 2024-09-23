package com.push.service.upstream.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.bo.platform.ListUpstreamBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.UpstreamBillInfoVO;
import com.push.dao.mapper.upstream.UpstreamBillInfoMapper;
import com.push.entity.upstream.UpstreamBillInfo;
import com.push.service.upstream.UpstreamBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 上游账单表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class UpstreamBillInfoServiceImpl extends ServiceImpl<UpstreamBillInfoMapper, UpstreamBillInfo> implements UpstreamBillInfoService, IExcelExportServer {

    @Resource
    private UpstreamBillInfoMapper upstreamBillInfoMapper;

    @Override
    public UpstreamBillInfo queryBillInfoByTradeNo(String tradeNo) {
        LambdaQueryWrapper<UpstreamBillInfo> wrapper = Wrappers.lambdaQuery(UpstreamBillInfo.class);
        wrapper.eq(UpstreamBillInfo::getTradeNo, tradeNo);
        return upstreamBillInfoMapper.selectOne(wrapper);
    }

    @Override
    public IPage<UpstreamBillInfoVO> listUpstreamBill(ListUpstreamBillBO bo) {
        IPage<UpstreamBillInfoVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamBillInfoMapper.listPlatformBill(page, bo);
    }

    @Override
    public List<ListBillAllTypeStatisticsVO> upstreamBillStatistics(ListUpstreamBillBO bo) {
        return upstreamBillInfoMapper.upstreamBillStatistics(bo);
    }

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        ExportBillBO exportBillBO = (ExportBillBO) queryParams;
        int pageSize = 500;
        page = (page - 1) * pageSize;
        return new ArrayList<>(upstreamBillInfoMapper.getExportField(exportBillBO, page, pageSize));
    }
}
