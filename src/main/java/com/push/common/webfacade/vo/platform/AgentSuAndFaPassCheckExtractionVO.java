package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/27 13:47
 *
 *

 */
@Data
public class AgentSuAndFaPassCheckExtractionVO {

    private Long id;

    private String upstreamName;

    private String phoneNumber;

    private String cardUserName;

    private String bankCardNumber;

    private String bankName;

    private String branchBank;

    private BigDecimal extractionMoney;

    private Date createTime;
}
