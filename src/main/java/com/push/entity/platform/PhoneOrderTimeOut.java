package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 * Create DateTime: 2020/6/10 16:39
 *
 *

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PhoneOrderTimeOut extends BaseEntity {

    /**
     * 平台订单号
     */
    private String platformOrderNo;

    public PhoneOrderTimeOut() {
    }

    public PhoneOrderTimeOut(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }
}
