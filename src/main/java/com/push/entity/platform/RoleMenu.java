package com.push.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import com.push.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 角色菜单关联表
 * Create DateTime: 2020-03-25 17:31
 *
 *

 */
@Data
@TableName("role_menu")
public class RoleMenu extends BaseEntity {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
