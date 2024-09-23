package com.push.common.webfacade.vo.platform;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/6/4 16:20
 *
 * 

 */
@Data
public class ListBillAllTypeStatisticsVO {

    //订单收入金额
    private Integer type;
    //订单提现金额
    private BigDecimal billStatisticsMoney;
}
