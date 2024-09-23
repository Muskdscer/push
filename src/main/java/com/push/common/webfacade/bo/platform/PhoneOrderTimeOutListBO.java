package com.push.common.webfacade.bo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.entity.BasePageBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/6/10 17:47
 *
 * 

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PhoneOrderTimeOutListBO extends BasePageBO {

    /**
     * 平台订单号
     */
    private String platformOrderNo;

    /**
     * 上游订单号
     */
    private String upstreamOrderNo;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = DateConstant.DATE_FORMAT_NORMAL, timezone = DateConstant.TIME_ZONE)
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = DateConstant.DATE_FORMAT_NORMAL, timezone = DateConstant.TIME_ZONE)
    private Date endTime;

    /**
     * 推送状态
     */
    private String pushStatus;

    /**
     * 商户名称
     */
    private String shopName;

    /**
     * 渠道名称
     */
    private String upstreamName;

    @Override
    public void validate() {
        super.validate();
    }
}
