package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.vo.platform.PlatformUserInfoVO;
import com.push.dao.mapper.platform.PlatformUserMapper;
import com.push.dao.platform.PlatformUserDao;
import com.push.entity.platform.PlatformUserInfo;
import com.push.service.platform.PlatformUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020-03-26 14:53
 *
 * 

 */
@Service
public class PlatformUserServiceImpl extends ServiceImpl<PlatformUserMapper, PlatformUserInfo> implements PlatformUserService {

    @Resource
    private PlatformUserDao platformUserDao;

    @Override
    public Page<PlatformUserInfoVO> queryPlatformUser(Long pageNo, Long pageSize) {
        return platformUserDao.selectPlatformUserList(pageNo, pageSize);
    }

}
