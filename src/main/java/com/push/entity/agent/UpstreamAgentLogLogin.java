package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 上游代理商登录日志表
 * </p>
 *
 * 

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UpstreamAgentLogLogin extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录用户id
     */
    private Long upstreamAgentUserId;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录状态  0->登录失败，1->登录成功
     */
    private Integer loginStatus;

}
