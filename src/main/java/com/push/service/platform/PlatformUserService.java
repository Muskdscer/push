package com.push.service.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.vo.platform.PlatformUserInfoVO;
import com.push.entity.platform.PlatformUserInfo;

/**
 * Description:
 * Create DateTime: 2020-03-26 14:52
 *
 * 

 */
public interface PlatformUserService extends IService<PlatformUserInfo> {

    /**
     * 分页查询平台用户
     *
     * @param pageNo   页号
     * @param pageSize 页面大小
     * @return 平台用户列表
     */
    Page<PlatformUserInfoVO> queryPlatformUser(Long pageNo, Long pageSize);
}
