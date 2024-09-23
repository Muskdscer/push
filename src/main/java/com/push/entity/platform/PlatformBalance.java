package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 平台总余额表
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatformBalance extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 平台总余额
     */
    private BigDecimal platformBalance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenMoney;


}
