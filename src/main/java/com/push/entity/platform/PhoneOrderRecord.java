package com.push.entity.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.push.common.enums.OperatorChannelTypeEnum;
import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 可用订单表
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"shopUserId","upstreamUserId","deleteFlag"})
public class PhoneOrderRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 上游用户id
     */
    private Long upstreamUserId;

    /**
     * 商户id
     */
    private Long shopUserId;
    /**
     * 身份标识码
     */
    private String provinceCode;
    /**
     * 城市标识码
     */
    private String cityCode;
    /**
     * 手机号归属地省份
     */
    private String province;
    /**
     * 手机号归属地城市
     */
    private String city;
    /**
     * 通道id
     */
    private Long aisleId;

    /**
     * 上游订单号
     */
    private String upstreamOrderNo;

    /**
     * 平台订单号
     */
    private String platformOrderNo;

    /**
     * 商户订单号
     */
    private String shopOrderNo;

    /**
     * 网厅流水号
     */
    private String orderSn;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 手机运营商
     */
    private String phoneOperator;

    /**
     * 手机运营商通道类型  {@link OperatorChannelTypeEnum}
     */
    private String type;

    /**
     * 上游推送订单金额
     */
    private BigDecimal orderPrice;

    /**
     * 推送商户订单金额
     */
    private BigDecimal shopPrice;

    /**
     * 订单过期时间
     */
    private Integer orderExpireTime;

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
     * 平台订单状态
     * 0:未使用
     * 1:已推送
     * 2:消费完成
     * 3:消费失败
     * 4:订单超时
     * 5:上游响应失败（亏损）
     */
    private Integer platformOrderStatus;

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
     * MQ消息状态
     */
    private Integer msgStatus;

    /**
     * 上游用户用户名
     */
    private String upstreamName;

    /**
     * 下游用户用户名
     */
    private String shopName;
}
