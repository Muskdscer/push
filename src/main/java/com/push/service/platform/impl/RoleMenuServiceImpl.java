package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.dao.mapper.platform.RoleMenuMapper;
import com.push.entity.platform.MenuInfo;
import com.push.entity.platform.RoleMenu;
import com.push.service.platform.RoleMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description:
 * Create DateTime: 2020-04-17 13:29
 *
 * 

 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public Set<String> queryMenuInterfaceUriByRoleId(Long roleId) {
        List<MenuInfo> menuInfoList = roleMenuMapper.selectMenuByRoleId(roleId);
        if (CollectionUtils.isEmpty(menuInfoList)) {
            return Collections.emptySet();
        }

        List<String> interfaceUri = new ArrayList<>();
        menuInfoList.forEach(item -> {
            if (StringUtils.isNotBlank(item.getInterfaceUri())) {
                String[] split = item.getInterfaceUri().split(",");
                List<String> strings = Arrays.asList(split);
                interfaceUri.addAll(strings);
            }
        });
        return new HashSet<>(interfaceUri);
    }
}
