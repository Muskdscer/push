package com.push.dao.shop.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.push.dao.mapper.shop.ShopUserMapper;
import com.push.dao.shop.ShopUserDao;
import com.push.entity.shop.ShopUserInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Description: 商户Dao接口实现类
 * Create DateTime: 2020-03-26 11:27
 *
 * 

 */
@Repository
public class ShopUserDaoImpl implements ShopUserDao {

    @Resource
    private ShopUserMapper shopUserMapper;

    @Override
    public ShopUserInfo selectShopUserByUserNameAndPassword(String username, String password) {

        Wrapper<ShopUserInfo> wrapper = Wrappers.lambdaQuery(ShopUserInfo.class)
                .eq(ShopUserInfo::getUserName, username)
                .eq(ShopUserInfo::getPassword, password);
        return shopUserMapper.selectOne(wrapper);
    }

}
