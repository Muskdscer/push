package com.push.common.webfacade.vo.out;

import com.push.common.enums.OperatorChannelTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/4/29 10:30
 *
 * 

 */
@Data
@Accessors(chain = true)
public class UpstreamCheckOrderVO {

    /**
     * 手机运营商
     */
    private String phoneOperator;
    /**
     * 手机号
     */
    private String phoneNum;
    /**
     * 上游订单号
     */
    private String orderNo;
    /**
     * 平台订单号
     */
    private String bizOrderNo;
    /**
     * 订单价格
     */
    private BigDecimal orderPrice;
    /**
     * 手机运营商通道类型  {@link OperatorChannelTypeEnum}
     */
    private String type;

    /**
     * 订单状态 0 失败 1 成功 2:订单超时 3:处理中
     */
    private Integer orderStatus;
    /**
     * 手机号归属地(省份)
     */
    private String province;
    /**
     * 手机号归属地(城市)
     */
    private String city;

    /**
     * 网厅流水号
     */
    private String orderSn;

    /**
     * 支付证明
     */
    private String certificate;




}
