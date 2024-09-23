package com.push.common.webfacade.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Title: ErrorEnum.class
 * Description: 系统错误枚举
 * Create DateTime: 2018/6/13 14:57
 *
 *

 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {

    /**
     * 成功
     */
    SUCCESS("1", 1, "SUCCESS", "成功"),

    /**
     * 失败
     */
    FAILED("-1", -1, "FAILED", "失败"),

    //---------------- 一般错误（0001-0999） ----------------
    /**
     * 未知错误
     */
    UNKNOWN("0001", -1, "UNKNOWN", "未知错误"),

    /**
     * 功能关闭
     */
    FUNCTION_CLOSED("0002", -2, "FUNCTION_CLOSED", "该功能已暂时关闭，有问题请联系客服。"),

    /**
     * 无法找到这个资源
     */
    URI_NOT_FOUND("0003", -3, "URI_NOT_FOUND", "无法找到这个资源"),

    //---------------- 参数错误(1000-1999) ----------------
    /**
     * 公共参数缺失
     */
    COMMON_PARAMS_MISSING("1000", -1000, "COMMON_PARAMS_MISSING", "公共参数缺失"),

    /**
     * 必填参数为空
     */
    REQUIRED_PARAM_EMPTY("1001", -1001, "REQUIRED_PARAM_EMPTY", "必填参数为空"),

    /**
     * 枚举值非法
     */
    ENUM_VALUE_INVALID("1002", -1002, "ENUM_VALUE_INVALID", "枚举值非法"),

    /**
     * 数字字符串格式不正确
     */
    NUMBER_FORMAT_INVALID("1003", -1003, "NUMBER_FORMAT_INVALID", "数字字符串格式不正确"),

    /**
     * 数字值非法
     */
    NUMBER_VALUE_INVALID("1004", -1004, "NUMBER_VALUE_INVALID", "数字值非法"),

    /**
     * 手机号码格式不正确
     */
    MOBILE_VALUE_INVALID("1005", -1006, "MOBILE_VALUE_INVALID", "手机号码格式不正确"),

    //---------------- 数据访问错误（5000-5999） ----------------
    /**
     * 序号获取失败
     */
    SEQ_GET_FAILED("5001", -5001, "SEQ_GET_FAILED", "序号获取失败"),

    /**
     * 字段为空
     */
    FIELD_IS_NULL("5002", -5002, "FIELD_IS_NULL", "字段为空"),

    /**
     * 文件上传失败
     */
    UPLOAD_IMG_FAILD("5003", -5003, "UPLOAD_IMG_FAILD", "文件上传失败"),

    //---------------- 权限验证错误（6000-6999） ----------------

    /**
     * 非法的请求方法
     */
    METHOD_INVALID("6000", -6000, "METHOD_INVALID", "非法的请求方法"),

    /**
     * 没有权限访问接口
     */
    API_PERMISSION_INVALID("6001", -6001, "API_PERMISSION_INVALID", "没有权限访问接口"),

    /**
     * 没有权限访问数据
     */
    DATA_PERMISSION_INVALID("6002", -6002, "DATA_PERMISSION_INVALID", "没有权限访问数据"),

    /**
     * 验证码错误
     */
    VERIFY_CODE_INVALID("6003", -6003, "VERIFY_CODE_INVALID", "验证码错误"),

    /**
     * 验证码错误
     */
    TOKEN_INVALID("6004", -6004, "TOKEN_INVALID", "无效的TOKEN"),

    /**
     * 请重新登录
     */
    RE_LOGIN("6005", -6005, "RE_LOGIN", "请重新登录"),

    /**
     * 用户名或密码错误
     */
    USERNAME_PASSWORD_INVALID("6006", -6006, "USERNAME_PASSWORD_INVALID", "用户名或密码错误"),

    //---------------- 外部调用错误（7000-7999） ----------------

    /**
     * RPC调用失败
     */
    RPC_REQUEST_FAILED("7000", -7001, "RPC_REQUEST_FAILED", "RPC调用失败"),

    //---------------- 业务逻辑错误（8000-9999） ----------------
    COMMON_BUSINESS_ERROR("8000", -8000, "BUSINESS_COMMON_ERROR", "业务问题"),

    /**
     * 该商户下无该手机号
     */
    USER_NO_MOBILE("8001", -8001, "USER_NO_MOBILE", "该商户下无该手机号"),

    /**
     * 用户名已存在
     */
    USER_NAME_EXIST("8002", -8002, "USER_NAME_EXIST", "用户名已存在"),

    /**
     * 两次密码不一致
     */
    PASSWORD_NOT_EQUAL("8003", -8003, "PASSWORD_NOT_EQUAL", "两次密码不一致"),

    /**
     * 账号已被禁用
     */
    ACCOUNT_DISABLED("8004", -8004, "ACCOUNT_DISABLED", "账号已被禁用"),


    //----------------------------------------------------------

    ;

    /**
     * 错误前缀
     */
    private static final String ERROR_PREFIX = "0000";

    /**
     * 内部错误代码
     */
    private String internalErrorCode;

    /**
     * 外部错误码
     */
    private Integer externalErrorCode;

    /**
     * 错误名称
     */
    private String errorName;

    /**
     * 错误描述
     */
    private String errorDescription;


}
