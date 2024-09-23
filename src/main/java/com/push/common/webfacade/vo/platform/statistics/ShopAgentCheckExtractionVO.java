package com.push.common.webfacade.vo.platform.statistics;

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
public class ShopAgentCheckExtractionVO {
    private Long id;
    private String shopAgentName;
    private String userMobile;
    private String cardUserName;
    private String bankCardNumber;
    private String bankName;
    private String branchBank;
    private BigDecimal extractionMoney;
    @JsonFormat(pattern = DateConstant.DATE_FORMAT_NORMAL, timezone = DateConstant.TIME_ZONE)
    private Date createTime;
}
