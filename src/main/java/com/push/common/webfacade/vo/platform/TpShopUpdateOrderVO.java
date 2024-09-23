package com.push.common.webfacade.vo.platform;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/5/26 17:07
 *
 * 

 */
@Data
@Accessors(chain = true)
public class TpShopUpdateOrderVO {

    //平台订单号
    private String shopOrderNo;
    //网厅流水号
    private String orderSn;
    //平台订单号
    private String platformOrderNo;

}
