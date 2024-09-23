package com.push.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Description: 删除标识枚举
 * Create DateTime: 2020-03-26 14:30
 *
 *

 */
@Getter
@AllArgsConstructor
public enum MatchMoneyEnum {

    TEN(new BigDecimal(10), "十块"),

    TWENTY(new BigDecimal(20), "二十块"),

    THIRTY(new BigDecimal(30), "三十块"),

    FIFTY(new BigDecimal(50), "五十块"),

    ONE_HUNDRED(new BigDecimal(100), "一百块"),

    TWO_HUNDRED(new BigDecimal(200), "两百块"),

    THREE_HUNDRED(new BigDecimal(300), "三百块"),

    FIVE_HUNDRED(new BigDecimal(500), "五百块");

    private BigDecimal money;
    private String msg;

}
