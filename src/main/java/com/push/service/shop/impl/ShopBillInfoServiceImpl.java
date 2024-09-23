package com.push.service.shop.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.bo.platform.ListShopBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.ShopBillInfoVO;
import com.push.dao.mapper.shop.ShopBillInfoMapper;
import com.push.entity.shop.ShopBillInfo;
import com.push.service.shop.ShopBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商户账单表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class ShopBillInfoServiceImpl extends ServiceImpl<ShopBillInfoMapper, ShopBillInfo> implements ShopBillInfoService, IExcelExportServer {
    @Resource
    private ShopBillInfoMapper shopBillInfoMapper;

    @Override
    public ShopBillInfo queryBillInfoByTradeNo(String tradeNo) {
        LambdaQueryWrapper<ShopBillInfo> wrapper = Wrappers.lambdaQuery(ShopBillInfo.class);
        wrapper.eq(ShopBillInfo::getTradeNo, tradeNo);
        return shopBillInfoMapper.selectOne(wrapper);
    }

    @Override
    public IPage<ShopBillInfoVO> listShopBill(ListShopBillBO bo) {
        IPage<ShopBillInfoVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return shopBillInfoMapper.listPlatformBill(page, bo);
    }

    @Override
    public List<ListBillAllTypeStatisticsVO> shopBillStatistics(ListShopBillBO bo) {
        return shopBillInfoMapper.shopBillStatistics(bo);
    }

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        ExportBillBO exportBillBO = (ExportBillBO) queryParams;
        int pageSize = 500;
        page = (page - 1) * pageSize;
        return new ArrayList<>(shopBillInfoMapper.getExportField(exportBillBO, page, pageSize));
    }
}
