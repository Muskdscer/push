package com.push.service.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.ListShopBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.ShopBillInfoVO;
import com.push.entity.shop.ShopBillInfo;

import java.util.List;

/**
 * <p>
 * 商户账单表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopBillInfoService extends IService<ShopBillInfo> {

    ShopBillInfo queryBillInfoByTradeNo(String tradeNo);

    IPage<ShopBillInfoVO> listShopBill(ListShopBillBO bo);

    List<ListBillAllTypeStatisticsVO> shopBillStatistics(ListShopBillBO bo);
}
