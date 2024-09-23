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
public enum BlackPhoneEnum {

    AUTO(0, "自动添加"),

    MANUAL(1, "手动添加");

    private int code;
    private String msg;

}
