package com.push.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 删除标识枚举
 * Create DateTime: 2020-03-26 14:30
 *
 * 

 */
@Getter
@AllArgsConstructor
public enum ShopCallbackOrderStatusEnum {

    FAIL(0, "失败"),

    SUCCESS(1, "成功"),

    TIME_OUT(2,"订单检查超时");

    private int code;
    private String desc;


}
