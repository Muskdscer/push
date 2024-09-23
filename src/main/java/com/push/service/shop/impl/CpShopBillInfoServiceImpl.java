package com.push.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.shop.CpShopBillInfoMapper;
import com.push.entity.shop.CpShopBillInfo;
import com.push.service.shop.CpShopBillInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商户账单事务表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class CpShopBillInfoServiceImpl extends ServiceImpl<CpShopBillInfoMapper, CpShopBillInfo> implements CpShopBillInfoService {

    @Resource
    private CpShopBillInfoMapper cpShopBillInfoMapper;


    @Override
    public List<CpShopBillInfo> findAllByState(Integer status) {
        LambdaQueryWrapper<CpShopBillInfo> wrapper = Wrappers.lambdaQuery(CpShopBillInfo.class);
        wrapper.eq(CpShopBillInfo::getStatus, status);
        Page<CpShopBillInfo> page = new Page<>(1, 100, false);
        Page<CpShopBillInfo> cpShopBillInfoPage = cpShopBillInfoMapper.selectPage(page, wrapper);
        return cpShopBillInfoPage.getRecords();
    }
}
