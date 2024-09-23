package com.push.common.utils;

import com.push.common.webfacade.error.CommonException;

public class CheckUtils {

    public static void checkNull(Object o, String entityName) {
        if (o == null) {
            throw new CommonException(entityName);
        }
    }
}
