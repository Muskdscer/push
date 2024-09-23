package com.push.common.enums.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Description:
 * Create DateTime: 2020/4/24 13:53
 *
 *

 */
@Getter
@AllArgsConstructor
public enum YiChengOrderStatusEnum {

    PROCESSING_0("0", "处理中"),

    PROCESSING_1("1", "处理中"),

    SUCCESS("2", "成功"),

    FAILED("3", "失败"),

    PROCESSING_2("4", "充值锁定中(处理中)"),

    NOT_CONFIRM("9", "未确认"),
    ;

    String code;

    String msg;

    public static YiChengOrderStatusEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        Optional<YiChengOrderStatusEnum> first = Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst();
        if (first.isEmpty()) {
            return null;
        }
        return first.get();
    }
}
