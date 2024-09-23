package com.push.service.out;

import com.push.entity.platform.PhoneOrderRecord;
import com.push.entity.shop.ShopUserInfo;

/**
 * Description:
 * Create DateTime: 2020-04-21 15:04
 *
 * 

 */
public interface ShopProcessor {

    /**
     * 商户商户信息
     *
     * @param param 商户回调参数信息
     * @return 商户信息
     */
    ShopUserInfo getShopUserInfo(String param);

    /**
     * 校验商户回调参数信息
     *
     * @param param 商户回调参数信息
     * @return 校验结果 true/false
     */
    boolean validateParam(String param);

    /**
     * 获取订单信息
     *
     * @param param 商户回调参数信息
     * @return 订单信息
     */
    PhoneOrderRecord getPhoneOrderRecord(String param);

    /**
     * 验签
     *
     * @param param  商户回调参数信息
     * @param appKey 秘钥
     * @return 校验结果 true/false
     */
    boolean validateSign(String param, String appKey);

    /**
     * 商户回调后置处理
     *
     * @param param            商户回调参数信息
     * @param phoneOrderRecord 订单信息
     */
    void postProcess(String param, PhoneOrderRecord phoneOrderRecord);

    /**
     * 创建商户回调日志
     *
     * @param param      商户回调参数信息
     * @param shopUserId 商户ID
     * @param status     状态码
     * @param remark     备注信息
     */
    void createShopCallbackOrderLog(String param, Long shopUserId, Integer status, String remark);

    /**
     * 响应成功
     *
     * @return  响应信息
     */
    String responseSuccessResult();

    /**
     * 响应失败
     *
     * @return  响应信息
     */
    String responseFailedResult();
}
