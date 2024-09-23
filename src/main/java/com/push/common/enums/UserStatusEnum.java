package com.push.common.enums;

import lombok.Getter;

/**
 * Description: 用户状态枚举类
 * Create DateTime: 2020-03-17 09:48
 *
 * 

 */
@Getter
public enum UserStatusEnum {

    /**
     * 正常
     */
    ENABLE(1, "启用"),

    /**
     * 冻结
     */
    DISABLE(2, "禁用"),
    ;

    private Integer value;
    private String message;

    UserStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static UserStatusEnum getByValue(Integer value) {
        if (value != null) {
            for (UserStatusEnum enu : values()) {
                if (enu.value.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
