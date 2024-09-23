package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 登录日志表
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatformLogLogin extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 登录用户id
     */
    private Long userId;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录状态  0->登录失败，1->登录成功
     */
    private Integer loginStatus;

}
