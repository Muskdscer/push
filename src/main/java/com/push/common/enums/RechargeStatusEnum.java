package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/3/31 10:51
 *
 * 

 */
@Getter
public enum RechargeStatusEnum {


    TODO(0, "待处理"),

    SUCCESSS(2, "成功"),

    FAIL(1, "失败");


    private Integer code;
    private String message;

    RechargeStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RechargeStatusEnum getByValue(Byte value) {
        if (value != null) {
            for (RechargeStatusEnum enu : values()) {
                if (enu.code.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
