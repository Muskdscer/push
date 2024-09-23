package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 统计金额表
 * </p>
 *
 *

 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StatisticsBill extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 总金额
     */
    private BigDecimal sumMoney;

    /**
     * 用户类型1.平台2.商户3.上游
     */
    private Integer userType;

    /**
     * 操作类型1充值2提现3交易
     */
    private Integer handleType;

    /**
     * 状态(0失败1成功2总流水)
     */
    private Integer status;

    /**
     * 记录时间
     */
    private Date recordTime;


}
