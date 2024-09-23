package com.push.dao.platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.dao.mapper.platform.RoleMapper;
import com.push.dao.platform.RoleDao;
import com.push.entity.platform.RoleInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Description: 角色Dao实现类
 * Create DateTime: 2020-03-26 15:42
 *
 * 

 */
@Repository
public class RoleDaoImpl implements RoleDao {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Page<RoleInfo> selectRoleList(Long pageNo, Long pageSize) {
        LambdaQueryWrapper<RoleInfo> wrapper = Wrappers.lambdaQuery(RoleInfo.class).orderByDesc(RoleInfo::getCreateTime);
        return roleMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
    }
}
