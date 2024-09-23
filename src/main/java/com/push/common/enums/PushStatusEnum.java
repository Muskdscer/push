package com.push.common.enums;

import lombok.Getter;

/**
 * Description: 订单推送状态枚举类
 * Create DateTime: 2020-03-17 09:48
 *
 * 

 */
@Getter
public enum PushStatusEnum {

    /**
     * 未推送
     */
    NO_PUSH(0, "未推送"),

    /**
     * 已推送
     */
    PUSH(1, "已推送"),

    /**
     * 推送失败
     */
    FAILED_PUSH(2, "推送失败"),
    ;

    private Integer value;
    private String message;

    PushStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static PushStatusEnum getByValue(Integer value) {
        if (value != null) {
            for (PushStatusEnum enu : values()) {
                if (enu.value.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
