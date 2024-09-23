package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020-04-16 15:02
 *
 * 

 */
@Data
public class SystemArea extends BaseEntity {

    /**
     * 区号
     */
    private String code;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

}
