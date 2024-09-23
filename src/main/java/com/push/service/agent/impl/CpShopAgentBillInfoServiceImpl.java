package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.agent.CpShopAgentBillInfoMapper;
import com.push.entity.agent.CpShopAgentBillInfo;
import com.push.service.agent.CpShopAgentBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商户代理商账单事务表 服务实现类
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Service
public class CpShopAgentBillInfoServiceImpl extends ServiceImpl<CpShopAgentBillInfoMapper, CpShopAgentBillInfo> implements CpShopAgentBillInfoService {

    @Resource
    private CpShopAgentBillInfoMapper cpShopAgentBillInfoMapper;

    @Override
    public List<CpShopAgentBillInfo> findAllByState(Integer status) {
        LambdaQueryWrapper<CpShopAgentBillInfo> wrapper = Wrappers.lambdaQuery(CpShopAgentBillInfo.class);
        wrapper.eq(CpShopAgentBillInfo::getStatus,status);
        Page<CpShopAgentBillInfo> page = new Page<>(1, 100, false);
        Page<CpShopAgentBillInfo> cpShopAgentBillInfoPage = cpShopAgentBillInfoMapper.selectPage(page, wrapper);
        return cpShopAgentBillInfoPage.getRecords();
    }
}
