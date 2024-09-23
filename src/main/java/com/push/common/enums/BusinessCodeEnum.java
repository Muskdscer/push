package com.push.common.enums;

import lombok.Getter;


@Getter
public enum BusinessCodeEnum implements BaseCodeInfoInterface {
    LOGIN_SUCCESS("1", "登陆成功"),

    LOGIN_FAIL("2", "登陆失败"),

    OPERATION_SUCCESS("1", "操作成功"),

    OPERATION_FAIL("2", "操作失败"),

    USER_EXISTED("2", "该账号已存在"),

    USER_NOT_EXIST("2", "该账号不存在"),

    SHOP_NOT_EXIST("2", "该商户不存在"),

    UPSTREAM_NOT_EXIST("2", "该商户不存在"),

    RE_LOGIN("6", "请重新登陆"),

    USER_PASSWORD_ERROR("7", "账号密码不正确"),

    UPDATE_USER_NOT_EXIST("2", "修改的账号不存在"),

    APP_ID_NOT_EXIST("25202", "找不到该APPID"),

    SIGNATURE_VERIFICATION_FAILED("25203", "签名校验失败"),

    DATA_IS_NULL("25204", "data数据为空"),

    NO_PHONE_NUMBER("25205", "暂无号码库存"),

    NO_DATA("2", "没有获取到data数据"),

    NO_BALANCE("2", "余额不足"),

    NO_SET_PHONE_PRICE("25207", "没有配置号码单价"),

    NOT_SET_RETURN_SITE("25208", "商户没有配置短信推送地址"),

    PARAM_ERROR("2", "参数错误")
//    Signature verification failed
    ;

    private String code;
    private String data;

    BusinessCodeEnum(String code, String data) {
        this.code = code;
        this.data = data;
    }
}
