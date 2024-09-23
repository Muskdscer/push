package com.push.service.upstream;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.ListUpstreamBillBO;
import com.push.common.webfacade.vo.platform.ListBillAllTypeStatisticsVO;
import com.push.common.webfacade.vo.platform.UpstreamBillInfoVO;
import com.push.entity.upstream.UpstreamBillInfo;

import java.util.List;

/**
 * <p>
 * 上游账单表 服务类
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface UpstreamBillInfoService extends IService<UpstreamBillInfo> {

    UpstreamBillInfo queryBillInfoByTradeNo(String tradeNo);

    IPage<UpstreamBillInfoVO> listUpstreamBill(ListUpstreamBillBO bo);

    List<ListBillAllTypeStatisticsVO> upstreamBillStatistics(ListUpstreamBillBO bo);
}
