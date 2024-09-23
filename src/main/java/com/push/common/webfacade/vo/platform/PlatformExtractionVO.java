package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 10:35
 *
 * 

 */
@Data
public class PlatformExtractionVO {

    private Long id;
    private String realName;
    private String userMobile;
    private String cardUserName;
    private String bankCardNumber;
    private String bankName;
    private String branchBank;
    private BigDecimal extractionMoney;
    private Date createTime;
}
