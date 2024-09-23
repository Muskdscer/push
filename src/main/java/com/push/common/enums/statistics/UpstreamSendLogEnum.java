package com.push.common.enums.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 删除标识枚举
 * Create DateTime: 2020-03-26 14:30
 *
 * 

 */
@Getter
@AllArgsConstructor
public enum UpstreamSendLogEnum {

    REDIS_BLACK_PHONE("1", "一分钟内重复发送"),

    UPSTREAM_USER_NOT_EXIST("2", "渠道用户不存在"),

    UPSTREAM_USER_DISABLE("3", "渠道用户被禁用"),

    UPSTREAM_USER_NOT_IN_WHITE_IP("4", "渠道不在白名单内"),

    ORDER_PARAM_FAIL("5", "订单参数错误"),

    SIGN_FAIL("6", "验签失败"),

    PHONE_NUM_FAIL("7", "手机号验证失败"),

    DB_BLACK_PHONE("8", "手机号在黑名单内"),

    ADD_NOT_OPEN("9", "地区未开放"),

    SYSTEM_FAIL("10", "系统错误"),

    NOT_MATCH_AISLE("11", "无法匹配通道"),

    UPSTREAM_BALANCE_INSUFFICIENT("12",  "渠道授信金额不足"),
    ;

    private String code;
    private String msg;

}
