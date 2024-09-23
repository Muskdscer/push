package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Create DateTime: 2020-04-16 17:13
 *
 * 

 */
@Data
public class SystemProvinceCity extends BaseEntity {

    /**
     * 省Code
     */
    private String provinceCode;


    /**
     * 市Code
     */
    private String cityCode;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 运营商
     */
    private String phoneOperator;

}
