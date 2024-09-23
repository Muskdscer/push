package com.push.service.upstream.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.upstream.CpUpstreamBillInfoMapper;
import com.push.entity.upstream.CpUpstreamBillInfo;
import com.push.service.upstream.CpUpstreamBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 上游账单事务表 服务实现类
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Service
public class CpUpstreamBillInfoServiceImpl extends ServiceImpl<CpUpstreamBillInfoMapper, CpUpstreamBillInfo> implements CpUpstreamBillInfoService {

    @Resource
    private CpUpstreamBillInfoMapper cpUpstreamBillInfoMapper;


    //查找所有未消费的账单事务表记录
    @Override
    public Page<CpUpstreamBillInfo> findAllByState(Integer status) {
        LambdaQueryWrapper<CpUpstreamBillInfo> wrapper = Wrappers.lambdaQuery(CpUpstreamBillInfo.class);
        wrapper.eq(CpUpstreamBillInfo::getStatus, status);
        Page<CpUpstreamBillInfo> objectPage = new Page<>(1, 100, false);
        return cpUpstreamBillInfoMapper.selectPage(objectPage, wrapper);
    }
}
