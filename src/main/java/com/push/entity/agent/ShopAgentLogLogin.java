package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商户代理商登录日志表
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShopAgentLogLogin extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录ip
     */
    private String loginIp;

    private Long UserId;

    /**
     * 登录状态  0->登录失败，1->登录成功
     */
    private Integer loginStatus;


}
