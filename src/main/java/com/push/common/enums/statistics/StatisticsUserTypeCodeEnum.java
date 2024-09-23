package com.push.common.enums.statistics;

import lombok.Getter;


@Getter
public enum StatisticsUserTypeCodeEnum {

    PLATFORM_USER(1,"平台"),
    SHOP_USER(2,"商户"),
    UPSTREAM_USER(3,"渠道"),
    SHOP_AGENT_USER(4,"商户代理商"),
    UPSTREAM_AGENT_USER(5,"渠道代理商");

    private Integer code;
    private String data;

    StatisticsUserTypeCodeEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
