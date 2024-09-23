package com.push.common.webfacade.vo.platform;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 9:48
 *
 * 

 */
@Data
public class ShopUserInfoVO {

    private Long id;

    private Long matchClassifyId;

    private String userName;

    private String name;

    /**
     * 商户企业名称
     */
    private String shopName;

    private String phoneNumber;

    private Double rate;

    private BigDecimal balance;

    //银行卡号
    private String bankCardNumber;

    //银行名称
    private String bankName;

    //支行名称
    private String registBankName;

    //持卡人姓名
    private String cardUserName;

    /**
     * 商户订单回调地址，用于推送
     */
    private String querySite;

    //白名单
    private String whiteIp;

    //推送地址
    private String pushSite;
    //推送数量
    private Integer pushNum;

    //订单价格
    private BigDecimal orderPrice;

    //订单过期时间
    private Integer orderExpire;

    //冻结金额
    private BigDecimal frozenMoney;

    //警告阀值
    private Long alarmNumber;

    private String appId;

    private String appKey;

    private Long roleId;

    private Integer status;

    private Date createTime;

    private Date updateTime;


}
