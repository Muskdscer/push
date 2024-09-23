package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.upstream.UpstreamAisleConfig;

/**
 * Description:
 * Create DateTime: 2020/8/18 16:36
 *
 *

 */
public interface UpstreamAisleConfigService extends IService<UpstreamAisleConfig> {

    /**
     * 根据渠道ID和运营商查询渠道通道配置信息
     *
     * @param upstreamUserId 渠道用户ID
     * @param phoneOperator  手机运营商
     * @return 渠道通道配置信息
     */
    UpstreamAisleConfig getAisleByUpstreamUserIdAndOperator(Long upstreamUserId, String phoneOperator);

    /**
     * 根据渠道用户ID和通道ID以及运营商获取对应费率
     *
     * @param upstreamUserId 渠道用户ID
     * @param aisleId        通道ID
     * @param operator       运营商
     * @return 费率
     */
    Double getRateByUpstreamUserIdAndAisleIdAndOperator(Long upstreamUserId, Long aisleId, String operator);
}
