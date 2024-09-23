package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商户->代理商映射关系表
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShopAgentMapInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户Id
     */
    private Long shopId;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 对应商户费率
     */
    private Double rate;


}
