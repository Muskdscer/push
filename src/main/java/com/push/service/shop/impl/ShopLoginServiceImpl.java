package com.push.service.shop.impl;

import com.push.dao.shop.ShopUserDao;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: 商户登录service接口实现类
 * Create DateTime: 2020-03-27 09:07
 *
 *

 */
@Service
public class ShopLoginServiceImpl implements ShopLoginService {

    @Resource
    private ShopUserDao shopUserDao;

    @Override
    public ShopUserInfo shopLogin(String username, String password) {
        return shopUserDao.selectShopUserByUserNameAndPassword(username, password);
    }
}
