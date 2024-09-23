package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.out.UpstreamSendOrderBO;
import com.push.entity.platform.SystemBlackPhone;

/**
 * Description:
 * Create DateTime: 2020/6/28 17:29
 *
 *

 */
public interface SystemBlackPhoneService extends IService<SystemBlackPhone> {

    /**
     * 保存手机黑名单
     *
     * @param bo 请求参数
     */
    void saveAndReload(UpstreamSendOrderBO bo);
}
