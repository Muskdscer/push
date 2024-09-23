package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Description:
 * Create DateTime: 2020/4/20 16:18
 *
 *

 */
@Data
@Accessors(chain = true)
public class ShopMatchClassify extends BaseEntity {

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
