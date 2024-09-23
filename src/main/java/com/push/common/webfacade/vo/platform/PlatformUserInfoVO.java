package com.push.common.webfacade.vo.platform;

import com.push.entity.BaseEntity;
import lombok.Data;

import java.util.Set;

/**
 * Description: 平台用户详情实体
 * Create DateTime: 2020-03-25 18:05
 *
 *

 */
@Data
public class PlatformUserInfoVO extends BaseEntity {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 账号状态
     */
    private Integer status;

    private String roleName;

    private Set<String> interfaceList;

}
