package com.push.common.webfacade.error;

/**
 * Title: CommonException.class
 * Description: 系统内部异常
 * Create DateTime: 2018/6/13 11:24
 *
 * 

 */
public class CommonException extends RuntimeException {

    /**
     * 错误枚举
     */
    private ErrorEnum error;

    /**
     * 错误详情
     */
    private String detail;

    /**
     * 系统内部异常类构造器
     *
     * @param error 错误枚举
     */
    public CommonException(ErrorEnum error) {
        this(error, error.getErrorDescription());
    }

    /**
     * 系统内部异常类构造器
     *
     * @param error 错误枚举
     */
    public CommonException(ErrorEnum error, Throwable throwable) {
        super(error.getErrorDescription(), throwable);

        this.error = error;
        this.detail = error.getErrorDescription();
    }

    /**
     * 系统内部异常类构造器
     *
     * @param error  错误枚举
     * @param detail 错误详情
     */
    public CommonException(ErrorEnum error, String detail) {
        super(detail);

        this.error = error;
        this.detail = detail;
    }

    /**
     * 系统内部异常类构造器（公共业务问题）
     *
     * @param detail
     */
    public CommonException(String detail) {
        super(detail);
        this.error = ErrorEnum.COMMON_BUSINESS_ERROR;
        this.detail = detail;
    }

    public ErrorEnum getError() {
        return error;
    }

    public void setError(ErrorEnum error) {
        this.error = error;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
