package com.push.common.enums;

import lombok.Getter;

/**
 * Description: 地区状态枚举类
 * Create DateTime: 2020-03-17 09:48
 *
 *

 */
@Getter
public enum AreaStatusEnum {

    /**
     * 正常
     */
    ENABLE(1, "启用"),

    /**
     * 冻结
     */
    DISABLE(0, "禁用"),
    ;

    private Integer value;
    private String message;

    AreaStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static AreaStatusEnum getByValue(Integer value) {
        if (value != null) {
            for (AreaStatusEnum enu : values()) {
                if (enu.value.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
