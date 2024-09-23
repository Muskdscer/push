package com.push.common.annotation;

import com.push.common.enums.OperationTypeEnum;
import com.push.common.enums.UserTypeCodeEnum;

import java.lang.annotation.*;

/**
 * Description:
 * Create DateTime: 2020-04-07 13:15
 *
 *

 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Operation {
    /**
     * 用户类型
     */
    UserTypeCodeEnum userType();

    /**
     * 操作类型
     */
    OperationTypeEnum operationType();

    /**
     * 操作描述
     */
    String operationDescribe() default "";

}
