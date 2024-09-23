package com.push.common.enums;

import lombok.Getter;

@Getter
public enum AlarmFlagEnum {


    UNALARM(0, "不警告"),

    ALARMED(1, "开始警告");


    private Integer code;
    private String message;

    AlarmFlagEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static AlarmFlagEnum getByValue(Byte value) {
        if (value != null) {
            for (AlarmFlagEnum enu : values()) {
                if (enu.code.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }


}
