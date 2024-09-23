package com.push.common.enums.statistics;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/4/22 16:29
 *
 * 

 */
@Getter
public enum StatisticsStatusEnum {
    FAIL(0, "失败"),
    SUCCESS(1, "成功"),
    TOTAL_MONEY(2, "总流水");

    private Integer code;
    private String data;

    StatisticsStatusEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
