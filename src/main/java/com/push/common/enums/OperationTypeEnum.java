package com.push.common.enums;

import lombok.Getter;

/**
 * Description: 操作类型枚举类
 * Create DateTime: 2020-03-17 09:48
 *
 * 

 */
@Getter
public enum OperationTypeEnum {

    UPSTREAM_RECHARGE("渠道充值"),

    UPSTREAM_EXTRACTION("渠道提现"),

    SHOP_EXTRACTION("商户提现"),

    SHOP_AGENT_EXTRACTION("商户代理商提现"),

    UPSTREAM_AGENT_EXTRACTION("渠道代理商提现"),

    PLATFORM_EXTRACTION("平台提现"),

    PLATFORM_USER_INFO("平台用户中心"),

    UPSTREAM_USER_INFO("渠道用户中心"),

    SHOP_USER_INFO("商户用户中心"),

    SHOP_AGENT_USER_INFO("商户代理商用户中心"),

    UPSTREAM_AGENT_USER_INFO("渠道代理商用户中心"),

    PLATFORM_MATCH_CLASSIFY("平台配比分类"),

    PLATFORM_MATCH("平台配比"),

    ROLE("角色");

    private String message;

    OperationTypeEnum(String message) {
        this.message = message;
    }
}
