package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 11:57
 *
 * 

 */
@Data
public class UpstreamAgentUserInfoVO {

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
    private String upstreamName;

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

    /**
     * 银行卡号
     */
    private String bankCardNumber;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 支行名称
     */
    private String branchBank;

    /**
     * 持卡人姓名
     */
    private String cardName;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
