package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/4/1 10:41
 *
 * 

 */
@Getter
public enum UpstreamOrderLogEnum {

    SUCCESSS(1, "成功"),

    FAIL(0, "失败");


    private Integer code;
    private String message;

    UpstreamOrderLogEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
