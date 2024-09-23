package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/4/2 13:12
 *
 *

 */
@Getter
public enum PlatformBillStatusEnum {

    UNBILL(0, "未记账"),

    BILLED(1, "以记账"),

    NOTBILL(2, "不记账");
    private Integer code;
    private String data;

    PlatformBillStatusEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
