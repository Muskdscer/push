package com.push.dao.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.bo.platform.ExportBillBO;
import com.push.common.webfacade.bo.platform.ListShopBillBO;
import com.push.common.webfacade.vo.platform.ExportShopBillVO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.ShopBillInfoVO;
import com.push.entity.shop.ShopBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商户账单表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopBillInfoMapper extends BaseMapper<ShopBillInfo> {

    IPage<ShopBillInfoVO> listPlatformBill(@Param("page") IPage<ShopBillInfoVO> page, @Param("bo") ListShopBillBO bo);

    List<ExportShopBillVO> getExportField(@Param("bo") ExportBillBO bo, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    void saveBatch(@Param("shopBillInfos") List<ShopBillInfo> shopBillInfos);

    List<ListBillAllTypeStatisticsVO> shopBillStatistics(@Param("bo") ListShopBillBO bo);
}
