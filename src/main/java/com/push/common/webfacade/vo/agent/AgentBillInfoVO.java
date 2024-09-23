package com.push.common.webfacade.vo.agent;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 19:44
 *
 *

 */
@Data
public class AgentBillInfoVO {

    private Long id;

    private String userName;

    private String tradeNo;

    private BigDecimal balance;

    private BigDecimal lastBalance;

    private BigDecimal frozeMoney;

    private BigDecimal waitMoney;

    private Date lastTime;

    private Integer type;

    private BigDecimal money;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
