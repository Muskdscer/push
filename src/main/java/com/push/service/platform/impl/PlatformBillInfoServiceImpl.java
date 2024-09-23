package com.push.service.platform.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.bo.platform.ListPlatFormBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.PlatformBillInfoVO;
import com.push.dao.mapper.platform.PlatformBillInfoMapper;
import com.push.entity.platform.PlatformBillInfo;
import com.push.service.platform.PlatformBillInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 账单表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
@Slf4j
public class PlatformBillInfoServiceImpl extends ServiceImpl<PlatformBillInfoMapper, PlatformBillInfo> implements PlatformBillInfoService, IExcelExportServer {

    @Resource
    private PlatformBillInfoMapper platformBillInfoMapper;


    @Override
    public PlatformBillInfo queryBillInfoByTradeNo(String tradeNo) {
        LambdaQueryWrapper<PlatformBillInfo> wrapper = Wrappers.lambdaQuery(PlatformBillInfo.class);
        wrapper.eq(PlatformBillInfo::getTradeNo, tradeNo);
        return platformBillInfoMapper.selectOne(wrapper);
    }

    @Override
    public IPage<PlatformBillInfoVO> listPlatformBill(ListPlatFormBillBO bo) {
        IPage<PlatformBillInfoVO> iPage = new Page<>(bo.getPageNo(), bo.getPageSize());
        return platformBillInfoMapper.listPlatformBill(iPage, bo);
    }

    @Override
    public List<ListBillAllTypeStatisticsVO> platformBillStatistics(ListPlatFormBillBO bo) {
        return platformBillInfoMapper.platformBillStatistics(bo);
    }

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        ExportBillBO exportBillBO = (ExportBillBO) queryParams;
        int pageSize = 500;
        page = (page - 1) * pageSize;
        return new ArrayList<>(platformBillInfoMapper.getExportField(exportBillBO, page, pageSize));
    }
}
