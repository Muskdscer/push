package com.push.dao.platform;

import com.push.entity.platform.PhoneOrderRecord;

import java.util.List;

/**
 * Description:
 * Create DateTime: 2020-04-10 11:45
 *
 *

 */
public interface PhoneOrderRecordDao {

    List<PhoneOrderRecord> selectPhoneOrderRecordByPlatformOrderNos(List<String> platformOrderNos);
}
