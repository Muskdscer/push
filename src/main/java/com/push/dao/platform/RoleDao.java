package com.push.dao.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.entity.platform.RoleInfo;

/**
 * Description: 角色Dao接口
 * Create DateTime: 2020-03-26 15:42
 *
 *

 */
public interface RoleDao {

    /**
     * 分页查询角色列表
     *
     * @param pageNo   页号
     * @param pageSize 页面大小
     * @return 角色列表
     */
    Page<RoleInfo> selectRoleList(Long pageNo, Long pageSize);

}
