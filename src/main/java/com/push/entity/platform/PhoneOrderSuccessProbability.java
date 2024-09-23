package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/4/23 17:58
 *
 *

 */
@Data
@Accessors(chain = true)
public class PhoneOrderSuccessProbability extends BaseEntity {

    /**
     * 成功率
     */
    private Double successProbability;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 记录时间
     */
    private String recordDate;
}
