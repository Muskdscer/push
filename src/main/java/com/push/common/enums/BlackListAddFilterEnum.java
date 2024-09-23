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
public enum BlackListAddFilterEnum {

    CLOSE("0", "关"),

    OPEN("1", "开");

    private String value;
    private String msg;

}
