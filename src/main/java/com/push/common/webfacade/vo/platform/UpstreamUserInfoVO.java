package com.push.common.webfacade.vo.platform;

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
public class UpstreamUserInfoVO {

    private Long id;

    private Long matchClassifyId;

    private String userName;

    private String name;

    private Long roleId;

    private String upstreamName;

    private String phoneNumber;

    private Double rate;

    private BigDecimal balance;

    //银行卡号
    private String bankCardNumber;

    //银行名称
    private String bankName;

    //支行名称
    private String branchBank;

    //持卡人姓名
    private String cardName;

    //白名单
    private String whiteIp;

    //推送地址
    private String pushSite;
    //推送数量
    private Integer pushNum;

    //冻结金额
    private BigDecimal frozenMoney;

    //警告阀值
    private Long alarmNumber;

    private String appId;

    private String appKey;

    /**
     * 账号状态
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;
}
