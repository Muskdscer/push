package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.platform.PhoneOrderTimeOutMapper;
import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.platform.PhoneOrderTimeOut;
import com.push.service.platform.PhoneOrderTimeOutService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/6/10 16:49
 *
 *

 */
@Service
public class PhoneOrderTimeOutServiceImpl extends ServiceImpl<PhoneOrderTimeOutMapper, PhoneOrderTimeOut> implements PhoneOrderTimeOutService {

    @Resource
    private PhoneOrderTimeOutMapper phoneOrderTimeOutMapper;

    @Override
    public void removeByOrderStatus() {
        phoneOrderTimeOutMapper.deleteByOrderStatus();
    }

    @Override
    public Page<PhoneOrderRecord> queryOrderTimeOutList(String platformOrderNo, String upstreamOrderNo, String phoneNum, String pushStatus,
                                                        String shopName, String upstreamName,
                                                        Date startTime, Date endTime, Long pageNo, Long pageSize) {
        return phoneOrderTimeOutMapper.selectOrderTimeOutList(new Page<>(pageNo, pageSize), platformOrderNo,
                upstreamOrderNo, phoneNum, pushStatus, shopName, upstreamName, startTime, endTime);
    }
}
