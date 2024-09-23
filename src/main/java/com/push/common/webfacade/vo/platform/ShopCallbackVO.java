package com.push.common.webfacade.vo.platform;

import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/5/28 17:03
 *
 *

 */
@Data
public class ShopCallbackVO {
    //平台订单号
    private String platformOrderNo;
    //商户订单号
    private String shopOrderNo;
    //网厅流水号
    private String orderSn;
}
