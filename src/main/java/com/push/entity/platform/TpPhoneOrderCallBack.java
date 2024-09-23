package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 回调订单临时表
 * </p>
 *
 *

 * @since 2020-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TpPhoneOrderCallBack extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 平台订单号
     */
    private String platformOrderNo;

    /**
     * 商户订单号
     */
    private String shopOrderNo;

    /**
     * 网厅流水号
     */
    private String orderSn;

    /**
     * 状态,0失败，1成功   2订单检查超时
     */
    private Integer status;

    /**
     * 详情
     */
    private String message;

    /**
     * 凭证
     */
    private String certificate;


}
