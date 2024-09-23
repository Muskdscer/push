package com.push.service.schedule;

import com.push.entity.platform.PhoneOrderRecord;

/**
 * Description:
 * Create DateTime: 2020/4/2 17:34
 *
 *

 */
public interface UpstreamCallbackService {

    void handleUpstreamCallbackSuccess();

    void handleUpstreamCallbackFail();

    void repeatHandleFail(PhoneOrderRecord phoneOrderRecord);

    void repeatHandleSuccess(PhoneOrderRecord phoneOrderRecord, String result);
}
