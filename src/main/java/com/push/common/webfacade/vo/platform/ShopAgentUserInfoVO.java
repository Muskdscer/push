package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/26 16:06
 *
 *

 */
@Data
public class ShopAgentUserInfoVO {

    private Long id;
    /**
     * 商户代理商拥有者姓名
     */
    private String userName;

    /**
     * 商户代理商手机号
     */
    private String phoneNumber;

    /**
     * 商户代理商企业名称
     */
    private String shopAgentName;

    /**
     * 商户代理商余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenMoney;

    private Date createTime;

    private Date updateTime;

}
