package com.push.common.enums;

import lombok.Getter;


@Getter
public enum UserTypeCodeEnum {

    PLATFORM_USER("1","平台管理员"),
    SHOP_USER("2","商户用户"),
    UPSTREAM_USER("3","上游渠道用户"),
    SHOP_AGENT_USER("4","商户代理商用户"),
    UPSTREAM_AGENT_USER("5","商户代理商用户");


    private String code;
    private String data;

    UserTypeCodeEnum(String code, String data) {
        this.code = code;
        this.data = data;
    }
}
