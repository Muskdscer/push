package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 上游代理商用户提现表
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamAgentExtraction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上游用户代理商id
     */
    private Long upstreamAgentUserId;

    /**
     * 提现标识码
     */
    private String extractionNumber;

    /**
     * 用户手机号
     */
    private String userMobile;

    /**
     * 银行卡号
     */
    private String bankCardNumber;

    /**
     * 拥有者姓名
     */
    private String cardUserName;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 支行
     */
    private String branchBank;

    /**
     * 提现金额
     */
    private BigDecimal extractionMoney;

    /**
     * 提现订单状态：0 待完成  1 失败  2 成功
     */
    private Integer status;

    /**
     * 通道id
     */
    private Long aisleId;


}
