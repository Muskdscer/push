package com.push.entity.upstream;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 上游用户提现表
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamAisleConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上游代理商用户id
     */
    private Long upstreamUserId;
    /**
     * 通道id
     */
    private Long aisleId;

    /**
     * 运营商
     */
    private String operator;

    /**
     * 通道余额
     */
    private Double rate;
    /**
     * 是否开启授信
     */
    private Integer isCredit;


}
