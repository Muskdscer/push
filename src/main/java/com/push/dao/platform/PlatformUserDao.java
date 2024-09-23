package com.push.dao.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.PlatformUserInfoVO;
import com.push.entity.platform.PlatformUserInfo;

/**
 * Description: 平台用户Dao接口
 * Create DateTime: 2020-03-26 11:24
 *
 * 

 */
public interface PlatformUserDao {

    /**
     * 根据用户名和密码查询平台用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 平台用户信息
     */
    PlatformUserInfo selectPlatformUserByUserNameAndPassword(String username, String password);

    /**
     * 分页查询平台用户列表
     *
     * @param pageNo   页号
     * @param pageSize 页面大小
     * @return 平台用户列表
     */
    Page<PlatformUserInfoVO> selectPlatformUserList(Long pageNo, Long pageSize);
}
