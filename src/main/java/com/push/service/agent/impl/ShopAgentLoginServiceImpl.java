package com.push.service.agent.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.enums.DeleteFlagEnum;
import com.push.dao.mapper.agent.ShopAgentUserInfoMapper;
import com.push.entity.agent.ShopAgentUserInfo;
import com.push.service.agent.ShopAgentLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020/4/28 13:47
 *
 * 

 */
@Service
public class ShopAgentLoginServiceImpl extends ServiceImpl<ShopAgentUserInfoMapper, ShopAgentUserInfo> implements ShopAgentLoginService {

    @Resource
    private ShopAgentUserInfoMapper shopAgentUserInfoMapper;

    @Override
    public ShopAgentUserInfo shopAgentLogin(String username, String password) {
        return shopAgentUserInfoMapper.selectOne(Wrappers.lambdaQuery(ShopAgentUserInfo.class)
                .eq(ShopAgentUserInfo::getUserName,username)
                .eq(ShopAgentUserInfo::getPassword,password)
                .eq(ShopAgentUserInfo::getDeleteFlag, DeleteFlagEnum.NOT_DELETE.getCode()));
    }
}
