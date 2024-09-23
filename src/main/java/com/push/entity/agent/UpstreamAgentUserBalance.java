package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 上游用户提现表
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamAgentUserBalance extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上游代理商用户id
     */
    private Long upstreamAgentUserId;
    /**
     * 通道id
     */
    private Long aisleId;

    /**
     * 通道name
     */
    private String aisleName;

    /**
     * 通道余额
     */
    private BigDecimal balance;
    /**
     * 通道冻结余额
     */
    private BigDecimal frozenMoney;


}
