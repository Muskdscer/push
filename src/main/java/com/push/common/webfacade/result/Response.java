package com.push.common.webfacade.result;

import com.push.common.webfacade.error.ErrorEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Create DateTime: 2019-05-24 16:15
 *
 * 

 */
@Data
public class Response implements Serializable {

    private Integer code;
    private Boolean success;
    private String message;
    private Object data;

    public Response() {
    }

    public static Response success() {
        return success(null);
    }

    public static Response success(Object data) {
        return success(ErrorEnum.SUCCESS.getErrorDescription(), data);
    }

    public static Response success(String message, Object data) {
        Response response = new Response();
        response.setCode(ErrorEnum.SUCCESS.getExternalErrorCode());
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static Response failed(ErrorEnum errorEnum) {
        Response response = new Response();
        response.setCode(errorEnum.getExternalErrorCode());
        response.setSuccess(false);
        response.setMessage(errorEnum.getErrorDescription());
        response.setData(null);
        return response;
    }

    public static Response failed(String message) {
        return failed(ErrorEnum.FAILED.getExternalErrorCode(), message);
    }

    public static Response failed(Integer code, String message) {
        Response response = new Response();
        response.setCode(code);
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        return response;
    }
}
