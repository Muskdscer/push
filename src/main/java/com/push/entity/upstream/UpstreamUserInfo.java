package com.push.entity.upstream;

import com.baomidou.mybatisplus.annotation.TableField;
import com.push.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Description:
 * Create DateTime: 2020-03-26 11:12
 *
 *

 */
@Getter
@Setter
public class UpstreamUserInfo extends BaseEntity {

    private String userName;

    private String password;

    private Long aisleClassifyId;

    private Long matchClassifyId;

    @TableField(exist = false)
    private Long roleId;

    private String upstreamName;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 手机区号，默认86
     */
    private String areaCode;

    /**
     * 白名单ip，可以多个，中间用,隔开
     */
    private String whiteIp;

    /**
     * 费率
     */
    private Double rate;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenMoney;

    /**
     * 警报阀值
     */
    private Long alarmNumber;

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

    /**
     * 推送地址
     */
    private String pushSite;

    /**
     * 推送数量
     */
    private Long pushNum;

    /**
     * 上游标识
     */
    private String appId;

    /**
     * 接口秘钥
     */
    private String appKey;

    /**
     * 账号状态
     */
    private Integer status;


}
