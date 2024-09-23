package com.push.service.upstream;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.upstream.UpstreamBalance;

import java.math.BigDecimal;

/**
 * <p>
 * 上游余额表 服务类
 * </p>
 */
public interface UpstreamBalanceService extends IService<UpstreamBalance> {

    /**
     * 根据渠道ID和通道ID获取对应的余额
     *
     * @param upstreamUserId 渠道用户ID
     * @param aisleId        通道ID
     * @return 对应的余额
     */
    BigDecimal getBalanceByUpstreamUserIdAndAisleId(Long upstreamUserId, Long aisleId);
}
