package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/3/31 10:18
 *
 *

 */
@Getter
public enum OrderStatusCodeEnum {

    PUSH_FAILED(-1, "推送失败"),

    ORDER_UNUSED(0, "未使用"),

    SUBMITTED(1, "已推送"),

    CONSUMPTION_SUCCESS(2, "消费完成"),

    CONSUMPTION_FAIL(3, "消费失败"),

    ORDER_TIME_OUT(4, "订单超时"),

    UPSTREAM_RESPONSE_FAIL(5, "上游响应失败");

    private Integer code;
    private String data;

    OrderStatusCodeEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
