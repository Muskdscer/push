package com.push.common.enums;

import lombok.Getter;

@Getter
public enum BillInfoStatusEnum {


    TODO(0, "未消费"),

    SUCCESSS(1, "以消费");


    private Integer code;
    private String message;

    BillInfoStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static BillInfoStatusEnum getByValue(Byte value) {
        if (value != null) {
            for (BillInfoStatusEnum enu : values()) {
                if (enu.code.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }


}
