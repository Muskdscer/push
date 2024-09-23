package com.push.dao.platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.dao.mapper.platform.PhoneOrderRecordMapper;
import com.push.dao.platform.PhoneOrderRecordDao;
import com.push.entity.platform.PhoneOrderRecord;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020-04-10 11:45
 *
 *

 */
@Repository
public class PhoneOrderRecordDaoImpl implements PhoneOrderRecordDao {

    @Resource
    private PhoneOrderRecordMapper phoneOrderRecordMapper;

    @Override
    public List<PhoneOrderRecord> selectPhoneOrderRecordByPlatformOrderNos(List<String> platformOrderNos) {
        LambdaQueryWrapper<PhoneOrderRecord> queryWrapper = Wrappers.lambdaQuery(PhoneOrderRecord.class)
                .in(PhoneOrderRecord::getPlatformOrderNo, platformOrderNos);
        return phoneOrderRecordMapper.selectList(queryWrapper);
    }
}
