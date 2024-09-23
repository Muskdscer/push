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
 * 商户代理商信息表
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShopAgentUserInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户代理商拥有者姓名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 商户代理商企业名称
     */
    private String shopAgentName;

    /**
     * 商户代理商手机号
     */
    private String phoneNumber;

    /**
     * 手机号区号，默认86
     */
    private String areaCode;

    /**
     * 商户代理商余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenMoney;

    /**
     * 账号状态
     */
    private Integer status;

}
