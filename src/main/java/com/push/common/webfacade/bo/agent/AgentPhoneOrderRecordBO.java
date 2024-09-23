package com.push.common.webfacade.bo.agent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.entity.BasePageBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/31 9:38
 *
 *

 */
@Data
public class AgentPhoneOrderRecordBO extends BasePageBO {

    //平台订单号
    private String platformOrderNo;
    //上游订单号
    private String upstreamOrderNo;
    //商户订单号
    private String shopOrderNo;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //手机号
    private String phoneNum;
    //手机运营商
    private String phoneOperator;
    //订单状态
    private Integer platformOrderStatus;

    @Override
    public void validate() {
        super.validate();
    }
}
