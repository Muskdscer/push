package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.agent.CpUpstreamAgentBillInfoMapper;
import com.push.entity.agent.CpUpstreamAgentBillInfo;
import com.push.service.agent.CpUpstreamAgentBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 上游代理商账单事务表 服务实现类
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Service
public class CpUpstreamAgentBillInfoServiceImpl extends ServiceImpl<CpUpstreamAgentBillInfoMapper, CpUpstreamAgentBillInfo> implements CpUpstreamAgentBillInfoService {

    @Resource
    private CpUpstreamAgentBillInfoMapper cpUpstreamAgentBillInfoMapper;

    @Override
    public List<CpUpstreamAgentBillInfo> findAllByState(Integer status) {
        LambdaQueryWrapper<CpUpstreamAgentBillInfo> wrapper = Wrappers.lambdaQuery(CpUpstreamAgentBillInfo.class);
        wrapper.eq(CpUpstreamAgentBillInfo::getStatus,status);
        Page<CpUpstreamAgentBillInfo> page = new Page<>(1, 100, false);
        Page<CpUpstreamAgentBillInfo> cpUpstreamAgentBillInfoPage = cpUpstreamAgentBillInfoMapper.selectPage(page, wrapper);
        return cpUpstreamAgentBillInfoPage.getRecords();
    }
}
