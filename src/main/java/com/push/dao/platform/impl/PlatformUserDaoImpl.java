package com.push.dao.platform.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.PlatformUserInfoVO;
import com.push.dao.mapper.platform.PlatformUserMapper;
import com.push.dao.platform.PlatformUserDao;
import com.push.entity.platform.PlatformUserInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Description: 平台用户Dao接口实现类
 * Create DateTime: 2020-03-26 11:25
 *
 *

 */
@Repository
public class PlatformUserDaoImpl implements PlatformUserDao {

    @Resource
    private PlatformUserMapper platformUserMapper;

    @Override
    public PlatformUserInfo selectPlatformUserByUserNameAndPassword(String username, String password) {
        Wrapper<PlatformUserInfo> wrapper = Wrappers.lambdaQuery(PlatformUserInfo.class)
                .eq(PlatformUserInfo::getUserName, username)
                .eq(PlatformUserInfo::getPassword, password);
        return platformUserMapper.selectOne(wrapper);
    }

    @Override
    public Page<PlatformUserInfoVO> selectPlatformUserList(Long pageNo, Long pageSize) {
        return platformUserMapper.selectPlatformUserList(new Page<PlatformUserInfoVO>(pageNo, pageSize));
    }
}
