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
public class SystemProvince extends BaseEntity {

    /**
     * 省code
     */
    private String code;

    /**
     * 省名称
     */
    private String name;

}
