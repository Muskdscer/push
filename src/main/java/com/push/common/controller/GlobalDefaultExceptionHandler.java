package com.push.common.controller;

import com.push.common.webfacade.error.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020-04-17 19:20
 *
 * 

 */
@Slf4j
@RestControllerAdvice
public class GlobalDefaultExceptionHandler extends BaseController {

    /**
     * 拦截自定义错误信息
     *
     * @param exception CommonException
     * @return 错误信息
     */
    @ExceptionHandler(value = CommonException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> handlerNoHandlerFoundException(CommonException exception) {
        return returnResultMap(ResultMapInfo.ADDFAIL, exception.getMessage());
    }

    /**
     * 拦截NoHandlerFoundException
     *
     * @param exception NoHandlerFoundException
     * @return 错误信息
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handlerNoHandlerFoundException(NoHandlerFoundException exception) {
        return returnResultMap(ResultMapInfo.NOTFOUND, exception.getMessage());
    }

    /**
     * 拦截HttpRequestMethodNotSupportedException
     *
     * @param exception HttpRequestMethodNotSupportedException
     * @return 错误信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, Object> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return returnResultMap(ResultMapInfo.ADDFAIL, exception.getMessage());
    }

    /**
     * 拦截Exception
     *
     * @param exception Exception
     * @return 错误信息
     */
    /*@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> handlerException(Exception exception) {
        return returnResultMap(ResultMapInfo.UNKNOW, exception.getMessage());
    }*/

}
