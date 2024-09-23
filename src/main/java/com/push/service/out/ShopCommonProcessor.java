package com.push.service.out;

import com.push.entity.shop.ShopUserInfo;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/7/9 15:37
 *
 *

 */
public interface ShopCommonProcessor {

    /**
     * 校验商户的状态
     *
     * @param shopUserInfo 商户信息
     * @return 校验结果 true/false
     */
    boolean validateShopStatus(ShopUserInfo shopUserInfo);

    /**
     * 校验商户白名单
     *
     * @param whiteIp 白名单
     * @param ipAddr  请求IP
     * @return 校验结果 true/false
     */
    boolean validateWhiteIp(String whiteIp, String ipAddr);

    /**
     * 校验订单是否过期
     *
     * @param nowDate         当前日期
     * @param createTime      创建时间
     * @param orderExpireTime 过期时间（秒）
     * @param upBackNum       渠道回调次数
     * @return 校验结果 true/false
     */
    boolean validateOrderExpire(Date nowDate, Date createTime, Integer orderExpireTime, Integer upBackNum);

}
