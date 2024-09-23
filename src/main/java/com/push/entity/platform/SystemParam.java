package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020-04-16 11:22
 *
 *

 */
@Data
public class SystemParam extends BaseEntity {

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数值
     */
    private String value;

    /**
     * 备注
     */
    private String remark;
}
