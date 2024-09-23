package com.push.common.convert;

import com.push.common.enums.out.YiChengOrderStatusEnum;

/**
 * Description:
 * Create DateTime: 2020/4/26 13:16
 *
 * 

 */
public class YiChengOrderStatusConvert {

    public static Integer convertStatus(String sourceStatus) {
        if (YiChengOrderStatusEnum.SUCCESS.getCode().equals(sourceStatus)) {
            return 1;
        } else {
            return 0;
        }
    }
}
