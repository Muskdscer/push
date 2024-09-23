package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/4/1 10:41
 *
 * 

 */
@Getter
public enum ShopCallbackOrderLogEnum {

    SUCCESSS(1, "成功"),

    FAIL(0, "失败"),

    CHECK_TIME_OUT(2, "订单检查超时");


    private Integer code;
    private String message;

    ShopCallbackOrderLogEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
