package com.push.common.enums;

import lombok.Getter;

/**
 * Description: 操作状态枚举类
 * Create DateTime: 2020-03-17 09:48
 *
 * 

 */
@Getter
public enum OperationResultEnum {

    /**
     * 失败
     */
    FAILED(0, "失败"),

    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    ;

    private Integer value;
    private String message;

    OperationResultEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static OperationResultEnum getByValue(Integer value) {
        if (value != null) {
            for (OperationResultEnum enu : values()) {
                if (enu.value.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
