package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/31 10:55
 *
 *

 */
@Data
public class CheckRechargeVO {
    private Long id;
    private String upstreamName;
    private String userMobile;
    private BigDecimal money;
    private Date createTime;
    private Date updateTime;

}
