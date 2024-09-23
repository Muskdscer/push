package com.push.entity.upstream;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 上游用户充值表
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamUserRecharge extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * user表id
     */
    private Long upstreamUserId;

    /**
     * 用户流水
     */
    private BigDecimal money;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 提现订单状态：0 待完成  1 失败  2 成功
     */
    private Integer status;


    /**
     * 通道id
     */
    private Long aisleId;

}
