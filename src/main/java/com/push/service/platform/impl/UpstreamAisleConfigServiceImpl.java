package com.push.service.platform.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.enums.DeleteFlagEnum;
import com.push.common.webfacade.error.CommonException;
import com.push.dao.mapper.platform.UpstreamAisleConfigMapper;
import com.push.entity.upstream.UpstreamAisleConfig;
import com.push.service.platform.UpstreamAisleConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/8/18 16:37
 *
 * 

 */
@Slf4j
@Service
public class UpstreamAisleConfigServiceImpl extends ServiceImpl<UpstreamAisleConfigMapper, UpstreamAisleConfig> implements UpstreamAisleConfigService {

    @Resource
    private UpstreamAisleConfigMapper upstreamAisleConfigMapper;

    @Override
    public UpstreamAisleConfig getAisleByUpstreamUserIdAndOperator(Long upstreamUserId, String phoneOperator) {
        List<UpstreamAisleConfig> upstreamAisleConfigList = upstreamAisleConfigMapper.selectList(Wrappers.lambdaQuery(UpstreamAisleConfig.class)
                .eq(UpstreamAisleConfig::getUpstreamUserId, upstreamUserId)
                .eq(UpstreamAisleConfig::getOperator, phoneOperator)
                .eq(UpstreamAisleConfig::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));

        if (CollectionUtils.isEmpty(upstreamAisleConfigList)) {
            return null;
        }

        return upstreamAisleConfigList.get(RandomUtil.randomInt(upstreamAisleConfigList.size()));
    }

    @Override
    public Double getRateByUpstreamUserIdAndAisleIdAndOperator(Long upstreamUserId, Long aisleId, String operator) {
        UpstreamAisleConfig upstreamAisleConfig = upstreamAisleConfigMapper.selectOne(Wrappers.lambdaQuery(UpstreamAisleConfig.class)
                .eq(UpstreamAisleConfig::getUpstreamUserId, upstreamUserId)
                .eq(UpstreamAisleConfig::getAisleId, aisleId)
                .eq(UpstreamAisleConfig::getOperator, operator)
                .eq(UpstreamAisleConfig::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode())
                .select(UpstreamAisleConfig::getRate));

        if (upstreamAisleConfig == null) {
            log.error("【渠道通道配置】获取信息为空，渠道ID为:{}, 通道 ID为：{}, 运营商为：{}", upstreamUserId, aisleId, operator);
            throw new CommonException("渠道通道配置信息不存在");
        }

        return upstreamAisleConfig.getRate();
    }
}
