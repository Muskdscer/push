package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/4/2 13:12
 *
 *

 */
@Getter
public enum UpstreamCallbackEnum {

    FAIL(0, "回调失败"),

    SUCCESS(1, "回调成功");
    private Integer code;
    private String data;

    UpstreamCallbackEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
