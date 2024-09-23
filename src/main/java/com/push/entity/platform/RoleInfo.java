package com.push.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import com.push.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 角色实体
 * Create DateTime: 2020-03-25 17:31
 *
 * 

 */
@Data
@TableName("role_info")
public class RoleInfo extends BaseEntity {

    /**
     * 角色名称
     */
    private String roleName;

}
