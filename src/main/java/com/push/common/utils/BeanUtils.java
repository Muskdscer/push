package com.push.common.utils;

import java.util.function.Supplier;

public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static <T> T copyPropertiesChaining(Object source, T target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T> T copyPropertiesChaining(Object source, Supplier<T> supplier) {
        return copyPropertiesChaining(source, supplier.get());
    }

}
