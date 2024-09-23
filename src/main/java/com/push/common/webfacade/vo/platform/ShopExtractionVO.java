package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 18:20
 *
 *

 */
@Data
public class ShopExtractionVO {
    private Long id;
    private String shopName;
    private String userMobile;
    private String cardUserName;
    private String bankCardNumber;
    private String bankName;
    private String branchBank;
    private BigDecimal extractionMoney;
    private Date createTime;
}
