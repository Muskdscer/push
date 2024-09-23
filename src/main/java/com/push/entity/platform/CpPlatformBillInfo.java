package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 平台账单事务表
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CpPlatformBillInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值人或者提现人id
     */
    private Long traderUserId;

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
     * 操作前余额
     */
    private BigDecimal balance;

    /**
     * 操作后余额
     */
    private BigDecimal lastBalance;

    /**
     * 结余时间
     */
    private Date lastTime;

    /**
     * 冻结金额
     */
    private BigDecimal frozeMoney;

    /**
     * 待处理金额
     */
    private BigDecimal waitMoney;

    /**
     * 消费订单状态，1:以消费，0:未消费
     */
    private Integer status;

}
