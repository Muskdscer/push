package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020-04-16 17:37
 *
 *

 */
@Data
public class SystemCity extends BaseEntity {

    /**
     * 市编码
     */
    private String code;

    /**
     * 市名称
     */
    private String name;

}
