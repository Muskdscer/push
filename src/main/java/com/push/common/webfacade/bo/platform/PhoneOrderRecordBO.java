package com.push.common.webfacade.bo.platform;

import com.push.entity.BasePageBO;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/31 9:38
 *
 * 

 */
@Data
public class PhoneOrderRecordBO extends BasePageBO {

    //平台订单号
    private String platformOrderNo;
    //上游订单号
    private String upstreamOrderNo;
    //商户名
    private String shopName;
    //渠道名
    private String upstreamName;
    //商户订单号
    private String shopOrderNo;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //订单状态
    private Integer platformOrderStatus;
    //上游回调状态
    private Integer upstreamCallbackStatus;
    //下游回调状态
    private Integer shopCallbackStatus;
    //运营商 手机运营商100移动200联通300电信
    private String phoneOperator;
    //手机号
    private String phoneNum;

    private String pushStatus;

    @Override
    public void validate() {
        super.validate();
    }
}
