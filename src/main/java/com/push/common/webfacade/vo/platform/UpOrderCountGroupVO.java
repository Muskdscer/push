package com.push.common.webfacade.vo.platform;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Create DateTime: 2020/6/4 16:45
 *
 * 

 */
@Data
public class UpOrderCountGroupVO {

    private Long orderCount;

    private BigDecimal orderPrice = new BigDecimal(0);

    private Integer upstreamCallbackStatus;
}
