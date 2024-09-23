package com.push.common.enums;

import lombok.Getter;

/**
 * Description: 用户状态枚举类
 * Create DateTime: 2020-03-17 09:48
 *
 * 

 */
@Getter
public enum LoginStatusEnum {

    /**
     * 失败
     */
    FAILED(0, "登录失败"),

    /**
     * 成功
     */
    SUCCESS(1, "登录成功"),
    ;

    private Integer value;
    private String message;

    LoginStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static LoginStatusEnum getByValue(Integer value) {
        if (value != null) {
            for (LoginStatusEnum enu : values()) {
                if (enu.value.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
