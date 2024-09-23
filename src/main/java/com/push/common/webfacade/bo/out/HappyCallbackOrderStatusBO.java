package com.push.common.webfacade.bo.out;

import com.push.common.webfacade.bo.BaseBO;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2021-03-19 14:22
 *
 * 

 */
@Data
@ToString
public class HappyCallbackOrderStatusBO extends BaseBO {

    /**
     * 平台订单号
     */
    private String order_id;

    /**
     * 商户订单号
     */
    private String out_order_id;

    /**
     * appId
     */
    private String mchid;

    /**
     * 充值手机号
     */
    private String tel;

    /**
     * 充值金额
     */
    private BigDecimal price;

    /**
     * 交易结果
     */
    private String status;

    /**
     * 验签
     */
    private String sign;

}
