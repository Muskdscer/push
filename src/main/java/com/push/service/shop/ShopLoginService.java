package com.push.service.shop;

import com.push.entity.shop.ShopUserInfo;

/**
 * Description: 商户登录service接口
 * Create DateTime: 2020-03-27 09:07
 *
 * 

 */
public interface ShopLoginService {

    /**
     * 商户用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 商户用户详情
     */
    ShopUserInfo shopLogin(String username, String password);

}
