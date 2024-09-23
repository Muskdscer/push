package com.push.entity.shop;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商户提现表
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShopUserExtraction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 提现标识码
     */
    private String extractionNumber;

    /**
     * 商户id
     */
    private Long shopUserId;

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
     * 所属银行
     */
    private String bankName;

    /**
     * 开户行
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
