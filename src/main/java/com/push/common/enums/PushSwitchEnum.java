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
public enum PushSwitchEnum {

    CLOSE(1, "关闭"),

    OPEN(0, "打开");

    private int code;
    private String desc;

}
