package com.push.common.enums;

import lombok.Getter;

@Getter
public enum RechargeTypeEnum {


    QUICK(0, "快充"),

    SLOW(1, "慢充");


    private Integer code;
    private String message;

    RechargeTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
