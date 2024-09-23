package com.push.service.platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.platform.CpPlatformBillInfoMapper;
import com.push.entity.platform.CpPlatformBillInfo;
import com.push.service.platform.CpPlatformBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 平台账单事务表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class CpPlatformBillInfoServiceImpl extends ServiceImpl<CpPlatformBillInfoMapper, CpPlatformBillInfo> implements CpPlatformBillInfoService {

    @Resource
    private CpPlatformBillInfoMapper cpPlatformBillInfoMapper;

    @Override
    public List<CpPlatformBillInfo> findAllByState(Integer status) {
        LambdaQueryWrapper<CpPlatformBillInfo> wrapper = Wrappers.lambdaQuery(CpPlatformBillInfo.class);
        wrapper.eq(CpPlatformBillInfo::getStatus, status);
        Page<CpPlatformBillInfo> page = new Page<>(1, 100, false);
        Page<CpPlatformBillInfo> cpPlatformBillInfoPage = cpPlatformBillInfoMapper.selectPage(page, wrapper);
        return cpPlatformBillInfoPage.getRecords();
    }
}
