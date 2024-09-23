package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 * Create DateTime: 2020/5/28 10:17
 *
 *

 */
@Data
@Accessors(chain = true)
public class CpTpPhoneOrderCallBack extends BaseEntity {
    //平台订单号
    private String platformOrderNo;
    //商户订单号
    private String shopOrderNo;
    //网厅流水号
    private String orderSn;
    //订单状态
    private Integer status;
}
