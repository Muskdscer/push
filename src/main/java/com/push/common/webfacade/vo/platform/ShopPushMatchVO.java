package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/10 11:28
 *
 *

 */
@Data
public class ShopPushMatchVO {

    private Long id;

    private Long shopId;

    private String shopName;

    private BigDecimal money;

    private Long start;

    private Long end;

    private Long matchNum;

    private Integer currentNum;

    private Date createTime;

    private Date updateTime;
}
