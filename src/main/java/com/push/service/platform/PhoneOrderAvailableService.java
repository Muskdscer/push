package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.PhoneOrderAvailable;
import com.push.entity.platform.PhoneOrderRecord;

/**
 * <p>
 * 可用订单表 服务类
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface PhoneOrderAvailableService extends IService<PhoneOrderAvailable> {

    Boolean saveBatchByAvailableAndSaveBatchByRecord(PhoneOrderAvailable availableOrders,
                                                     PhoneOrderRecord recordOrders,
                                                     Long upstreamAisleClassifyId,
                                                     Integer rechargeType);

}
