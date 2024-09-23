package com.push.common.enums;

import lombok.Getter;

@Getter
public enum BillInfoTypeEnum {


    RECHARGE(1, "充值"),

    EXTRACTION(2, "提现"),

    SAVE(3, "积攒"),

    CONSUME(4, "消费"),

    SERVICE_CHARGE(5, "提现手续费收入");

    private Integer code;
    private String message;

    BillInfoTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static BillInfoTypeEnum getByValue(Byte value) {
        if (value != null) {
            for (BillInfoTypeEnum enu : values()) {
                if (enu.code.equals(value)) {
                    return enu;
                }
            }
        }
        return null;
    }


}
