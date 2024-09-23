package com.push.service.platform;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.ListPlatFormBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.PlatformBillInfoVO;
import com.push.entity.platform.PlatformBillInfo;

import java.util.List;

/**
 * <p>
 * 账单表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface PlatformBillInfoService extends IService<PlatformBillInfo> {


    PlatformBillInfo queryBillInfoByTradeNo(String tradeNo);

    IPage<PlatformBillInfoVO> listPlatformBill(ListPlatFormBillBO bo);

    List<ListBillAllTypeStatisticsVO> platformBillStatistics(ListPlatFormBillBO bo);
}
