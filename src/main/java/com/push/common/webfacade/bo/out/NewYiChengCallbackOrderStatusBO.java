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
public class NewYiChengCallbackOrderStatusBO extends BaseBO {

    /**
     * 平台订单号
     */
    private String user_order_id;

    /**
     * 商户订单号
     */
    private String sys_order_id;

    /**
     * 充值状态 200：成功 202：失败（注意：回调和查询接口状态码不同）
     */
    private String status;

    /**
     * 签名
     */
    private String sign;

}
