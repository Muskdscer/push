package com.push.common.webfacade.vo.out;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/4/2 17:50
 *
 * 

 */
@Data
@Accessors(chain = true)
public class UpstreamCallBackVO {
    /**
     * 渠道订单号
     */
    private String orderNo;

    /**
     * 平台订单号
     */
    private String bizOrderNo;
}
