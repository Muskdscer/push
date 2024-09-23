package com.push.common.webfacade.vo.platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description:
 * Create DateTime: 2020-04-01 13:24
 *
 * 

 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhoneOrderWaitCheck {

    /**
     * 查询地址
     */
    private String querySite;

    /**
     * 平台订单号
     */
    private String platformOrderNo;

    /**
     * 商户订单号
     */
    private String shopOrderNo;

    /**
     * 网厅流水号
     */
    private String orderSn;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 详细信息
     */
    private String message;

    /**
     * 凭证
     */
    private String certificate;

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * appId
     */
    private String appId;

    /**
     * appKey
     */
    private String appKey;


    public PhoneOrderWaitCheck(String querySite, String platformOrderNo, String classifyName) {
        this.querySite = querySite;
        this.platformOrderNo = platformOrderNo;
        this.classifyName = classifyName;
    }
    public PhoneOrderWaitCheck(String querySite, String platformOrderNo, String classifyName, String appId, String appKey) {
        this.querySite = querySite;
        this.platformOrderNo = platformOrderNo;
        this.classifyName = classifyName;
        this.appId = appId;
        this.appKey = appKey;
    }
}
