package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/3/31 10:18
 *
 * 

 */
@Getter
public enum UpstreamCallBackStatusEnum {

    FAILED(0, "订单消费失败"),

    SUCCESS(1, "订单消费成功"),

    ORDER_EXPIRE(2, "订单超时"),

    ORDER_PROCESSING(3, "订单处理中");

    private Integer code;
    private String data;

    UpstreamCallBackStatusEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
