package com.push.common.enums;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/3/30 9:57
 *
 * 

 */
@Getter
public enum ExtractionStatusEnum {

    TODO(0, "待处理"),

    SUCCESSS(2, "成功"),

    FAIL(1, "失败");


    private Integer code;
    private String message;

    ExtractionStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ExtractionStatusEnum getByValue(Byte value) {
        if (value != null) {
            for (ExtractionStatusEnum enu : values()) {
                if (enu.code.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
