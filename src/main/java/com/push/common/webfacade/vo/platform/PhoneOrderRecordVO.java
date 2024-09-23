package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/31 9:50
 *
 * 

 */
@Data
public class PhoneOrderRecordVO {

    private Long id;

    /**
     * 手机号归属地省份
     */
    private String province;
    /**
     * 手机号归属地城市
     */
    private String city;

    //上游订单号
    private String upstreamOrderNo;

    //平台订单号
    private String platformOrderNo;

    //下游订单号
    private String shopOrderNo;

    //手机号
    private String phoneNum;

    //手机运营商
    private String phoneOperator;

    //上游推送订单金额
    private BigDecimal orderPrice;

    //推送商户订单金额
    private BigDecimal shopPrice;

    //订单过期时间
    private Integer orderExpireTime;

    //台订单状态0:未使用1:已推送2:消费完成3:消费失败4:上游响应失败
    private Integer platformOrderStatus;

    /**
     * 网厅流水号
     */
    private String orderSn;


    /**
     * 订单过期状态0:未过期1:已过期
     */
    private Integer orderExpireStatus;

    /**
     * 推送状态0:未推送1:已推动
     */
    private Integer pushStatus;

    /**
     * 推送次数
     */
    private Integer pushNum;

    /**
     * 查询商户订单次数
     */
    private Integer queryNum;

    /**
     * 回调渠道次数
     */
    private Integer upBackNum;

    /**
     * 上游回调状态0:回调失败1回调成功
     */
    private Integer upstreamCallbackStatus;

    /**
     * 商户回调状态0:回调失败1回调成功
     */
    private Integer shopCallbackStatus;


    /**
     * 平台记账状态0:未记账1:已记账2:不记账
     */
    private Integer platformBillStatus;

    /**
     * 商户记账状态0:未记账1:已记账2:不记账
     */
    private Integer shopBillStatus;

    /**
     * 上游记账状态0:未记账1:已记账2:不记账
     */
    private Integer upstreamBillStatus;


    /**
     * 上游用户用户名
     */
    private String upstreamName;

    /**
     * 下游用户用户名
     */
    private String shopName;

    @JsonFormat(pattern = DateConstant.DATE_FORMAT_NORMAL, timezone = DateConstant.TIME_ZONE)
    private Date createTime;
    @JsonFormat(pattern = DateConstant.DATE_FORMAT_NORMAL, timezone = DateConstant.TIME_ZONE)
    private Date updateTime;

}
