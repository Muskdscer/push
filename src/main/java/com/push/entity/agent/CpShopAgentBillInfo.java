package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商户代理商账单事务表
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CpShopAgentBillInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值人或者提现人id
     */
    private Long traderUserId;

    /**
     * 对应商户id
     */
    private Long mapUserId;

    /**
     * 充值或者提现订单号
     */
    private String tradeNo;

    /**
     * 交易类型1:为充值，2:为提现，3:为积攒，4:为消费
     */
    private Integer type;

    /**
     * 充值或者提现金额
     */
    private BigDecimal money;

    /**
     * 消费订单状态，1:以消费，0:未消费
     */
    private Integer status;

    /**
     * 操作前余额
     */
    private BigDecimal balance;

    /**
     * 操作后余额
     */
    private BigDecimal lastBalance;

    /**
     * 最后一次结余时间
     */
    private Date lastTime;

    /**
     * 冻结金额
     */
    private BigDecimal frozeMoney;

    /**
     * 等待处理金额
     */
    private BigDecimal waitMoney;


}
