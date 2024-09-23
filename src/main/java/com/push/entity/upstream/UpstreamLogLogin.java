package com.push.entity.upstream;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 上游登录日志表
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamLogLogin extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录用户id
     */
    private Long upstreanUserId;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录状态  0->登录失败，1->登录成功
     */
    private Integer loginStatus;


}
