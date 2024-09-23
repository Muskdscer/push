package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/4/22 16:02
 *
 *

 */
@Data
@Accessors(chain = true)
public class PhoneOrderStatistics extends BaseEntity {

    private Integer orderResultNum;

    private Integer orderSuccessNum;

    private Integer orderFailNum;

    private String phoneOperator;

    private String statisticsDate;
}
