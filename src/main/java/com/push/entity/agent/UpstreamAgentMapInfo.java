package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 渠道->代理商映射关系表
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamAgentMapInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通道Id
     */
    private Long upstreamId;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 通道ID
     */
    private Long aisleId;

    /**
     * 对应渠道费率
     */
    private Double rate;

}
