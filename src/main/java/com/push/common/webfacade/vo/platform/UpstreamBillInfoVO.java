package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
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
public class UpstreamBillInfoVO {

    private Long id;

    private String userName;

    private String tradeNo;

    private Integer type;

    private BigDecimal balance;

    private BigDecimal lastBalance;

    private BigDecimal frozeMoney;

    private BigDecimal waitMoney;

    private Date lastTime;

    private BigDecimal money;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
