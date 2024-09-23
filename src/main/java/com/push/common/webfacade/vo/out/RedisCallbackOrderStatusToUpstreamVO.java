package com.push.common.webfacade.vo.out;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/2 15:40
 *
 *

 */
@Data
public class RedisCallbackOrderStatusToUpstreamVO {

    //渠道订单号
    private String appId;
    //签名
    private String sign;
    //运营商
    private String phoneOperator;
    //手机号
    private String phoneNum;
    //渠道订单号
    private String orderNo;
    //平台订单号
    private String bizOrderNo;
    //订单价格
    private BigDecimal orderPrice;
    //通道类型
    private String type;
    //手机号所属省
    private String province;
    //手机号所属市
    private String city;
    //订单消费状态
    private Integer status;
    //网厅流水号
    private String orderSn;
    //支付凭证
    private String certificate;
}
