package com.push.service.schedule;

import com.push.common.webfacade.bo.out.SjShopOrderNoBO;
import com.push.entity.platform.PhoneOrderAvailable;

import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/4/26 18:01
 *
 *

 */
public interface OrderService {

    void handlerFailedOrder(PhoneOrderAvailable phoneOrderAvailable, Boolean isException);
    
    void handlerPushOrderSuccessForSJ(List<SjShopOrderNoBO> sjShopOrderNoBOList);
}
