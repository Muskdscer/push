package com.push.common.webfacade.vo.out;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/4/30 9:33
 *
 *

 */
@Data
@Accessors(chain = true)
public class UpstreamSendOrderVO {

    /**
     * 上游订单号
     */
    private String orderNo;

    /**
     * 平台订单号
     */
    private String bizOrderNo;
}
