package com.push.common.webfacade.vo.out;

import com.push.entity.platform.PhoneOrderAvailable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/4/1 13:10
 *
 * 

 */
@Data
@Accessors(chain = true)
public class PhoneOrderWaitPush {

    /**
     * AppId
     */
    private String appId;

    /**
     * AppKey
     */
    private String appKey;

    /**
     * 推送地址
     */
    private String pushSite;

    /**
     * 订单
     */
    private PhoneOrderAvailable phoneOrderAvailable;

    /**
     * 费率
     */
    private double rate;

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * 商户回调地址
     */
    private String shopCallbackSite;
}
