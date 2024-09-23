package com.push.dao.mapper.platform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.PlatformUserInfoVO;
import com.push.entity.platform.PlatformUserInfo;

/**
 * Description:
 * Create DateTime: 2020-03-26 11:32
 *
 * 

 */
public interface PlatformUserMapper extends BaseMapper<PlatformUserInfo> {

    Page<PlatformUserInfoVO> selectPlatformUserList(Page<PlatformUserInfoVO> platformUserInfoVOPage);
}
