package com.push.service.platform;

import com.push.common.webfacade.bo.platform.PushOrderCallbackUpstreamBO;

/**
 * Description:
 * Create DateTime: 2020/5/21 10:38
 *
 *

 */
public interface PhoneOrderRecordPushService {

    /**
     * 平台管理员手动回调上游
     */
    void pushOrderCallbackUpstream(PushOrderCallbackUpstreamBO bo);
}
