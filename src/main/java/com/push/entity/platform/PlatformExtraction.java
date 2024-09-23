package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 上游用户提现表
 * </p>
 *
 * 

 * @since 2020-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatformExtraction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 平台用户id
     */
    private Long platformUserId;

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


}
