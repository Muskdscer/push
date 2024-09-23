package com.push.service.upstream.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.webfacade.error.CommonException;
import com.push.dao.mapper.upstream.UpstreamBalanceMapper;
import com.push.entity.upstream.UpstreamBalance;
import com.push.service.upstream.UpstreamBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 上游余额表 服务实现类
 * </p>
 */
@Slf4j
@Service
public class UpstreamBalanceServiceImpl extends ServiceImpl<UpstreamBalanceMapper, UpstreamBalance> implements UpstreamBalanceService {

    @Resource
    private UpstreamBalanceMapper upstreamBalanceMapper;

    @Override
    public BigDecimal getBalanceByUpstreamUserIdAndAisleId(Long upstreamUserId, Long aisleId) {
        UpstreamBalance upstreamBalance = upstreamBalanceMapper.selectOne(Wrappers.lambdaQuery(UpstreamBalance.class)
                .eq(UpstreamBalance::getUpstreamUserId, upstreamUserId)
                .eq(UpstreamBalance::getAisleId, aisleId)
                .eq(UpstreamBalance::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));

        if (upstreamBalance == null) {
            log.error("【获取渠道用户余额】获取为空，渠道ID为：{}, 通道ID为：{}", upstreamUserId, aisleId);
            throw new CommonException("渠道用户余额信息不存在");
        }
        return upstreamBalance.getBalance();
    }
}
