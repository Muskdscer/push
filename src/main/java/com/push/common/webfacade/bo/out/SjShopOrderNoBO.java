package com.push.common.webfacade.bo.out;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Create DateTime: 2020/9/5 16:13
 *
 * 

 */
@Data
public class SjShopOrderNoBO implements Serializable {

    /**
     * 平台订单号
     */
    private String platformOrderNo;

    /**
     * 商户订单号
     */
    private String shopOrderNo;

}
