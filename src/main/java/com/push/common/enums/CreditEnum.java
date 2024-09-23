package com.push.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/8/18 16:43
 *
 *

 */
@Getter
@AllArgsConstructor
public enum CreditEnum {

    ENABLE(1, "开启"),

    DISABLE(0, "禁用"),
    ;

    private int code;

    private String desc;
}
