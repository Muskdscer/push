package com.push.common.webfacade.vo.out;

import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/10 9:58
 *
 *

 */
@Data
public class MqMessage {
    /**
     * 平台订单还
     */
    private String platformOrderNo;

    /**
     * 查询地址
     */
    private String querySite;

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * 商户appId
     */
    private String appId;

    /**
     * 商户appKey
     */
    private String appKey;

}
