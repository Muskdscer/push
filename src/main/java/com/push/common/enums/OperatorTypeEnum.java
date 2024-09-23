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
public enum OperatorTypeEnum {

    MOBILE("100", "移动"),

    UNICOM("200", "联通"),

    TELECOM("300", "电信");

    private String code;

    private String desc;

}
