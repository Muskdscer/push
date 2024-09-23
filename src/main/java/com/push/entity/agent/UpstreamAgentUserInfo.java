package com.push.entity.agent;

import com.baomidou.mybatisplus.annotation.TableField;
import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 渠道代理商用户表
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamAgentUserInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 上游渠道代理商名称
     */
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

    /**
     * 账号状态
     */
    private Integer status;

}
