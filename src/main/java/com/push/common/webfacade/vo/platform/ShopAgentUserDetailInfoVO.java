package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/27 18:03
 *
 * 

 */
@Data
public class ShopAgentUserDetailInfoVO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 上游渠道代理商名称
     */
    private String shopAgentName;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenMoney;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
