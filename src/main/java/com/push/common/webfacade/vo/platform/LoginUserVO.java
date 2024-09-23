package com.push.common.webfacade.vo.platform;

import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020/4/30 10:33
 *
 *

 */
@Data
public class LoginUserVO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * token
     */
    private String token;

}
