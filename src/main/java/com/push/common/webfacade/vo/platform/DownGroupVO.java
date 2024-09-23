package com.push.common.webfacade.vo.platform;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/6/4 15:29
 *
 * 

 */
@Data
@Accessors(chain = true)
public class DownGroupVO {

    /**
     * 商户名
     */
    private String name;

    /**
     * 商户订单成功数
     */
    private Long orderSuccess = 0L;

    /**
     * 商户订单失败数
     */
    private Long orderFail = 0L;

    /**
     * 商户订单总数数
     */
    private Long orderSum = 0L;

    /**
     * 商户订单成功流水
     */
    private BigDecimal priceSuccess = new BigDecimal(0);

    /**
     * 商户订单失败流水
     */
    private BigDecimal priceFail = new BigDecimal(0);

    /**
     * 商户订单总流水
     */
    private BigDecimal priceSum = new BigDecimal(0);
}
