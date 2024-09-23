package com.push.common.enums.statistics;

import lombok.Getter;

/**
 * Description:
 * Create DateTime: 2020/4/22 16:30
 *
 *

 */
@Getter
public enum StatisticsHandleTypeEnum {
    RECHARGE(1, "充值"),
    EXTRACTION(2, "提现"),
    DEAL(3, "交易");

    private Integer code;
    private String data;

    StatisticsHandleTypeEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
