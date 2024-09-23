package com.push.service.upstream;

import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.upstream.UpstreamUserInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @since 2020-06-13
 */
public interface UpstreamCallBackService {

    /**
     * 回调渠道
     */
    void callbackUpstream(UpstreamUserInfo upstreamUserInfo, PhoneOrderRecord phoneOrderRecord,
                          String orderSn, String certificate, Integer status);

}
