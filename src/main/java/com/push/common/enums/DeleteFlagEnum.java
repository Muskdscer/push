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
public enum DeleteFlagEnum {

    DELETE(1, "已删除"),

    NOT_DELETE(0, "未删除");

    private int code;
    private String desc;

}
