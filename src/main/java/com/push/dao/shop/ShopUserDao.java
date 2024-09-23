package com.push.dao.shop;

import com.push.entity.shop.ShopUserInfo;

/**
 * Description: 商户Dao接口
 * Create DateTime: 2020-03-26 11:27
 *
 * 

 */
public interface ShopUserDao {

    /**
     * 根据用户名和密码查询商户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 商户信息
     */
    ShopUserInfo selectShopUserByUserNameAndPassword(String username, String password);
}
