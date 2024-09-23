package com.push.service.schedule;

import com.push.entity.platform.TpPhoneOrderCallBack;

/**
 * Description:
 * Create DateTime: 2020/4/2 9:22
 *
 *

 */
public interface ShopCallbackOrderStatusService {

    //消费商户回调成功
    void handleShopCallbackSuccessOrder();

    //消费商户回调成功
    //void handOrderCallBackSuccess(TpPhoneOrderCallBack tpPhoneOrderCallBack);

    void handleShopCallbackFailOrder();

    //void handleOrderCallBackFail(TpPhoneOrderCallBack tpPhoneOrderCallBack);
}
