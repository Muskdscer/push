package com.push.common.webfacade.vo.platform;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/23 13:08
 *
 *

 */
@Data
public class ExportPhoneOrderRecordVO {

    @Excel(name = "平台订单号", width = 25)
    private String platformOrderNo;

    @Excel(name = "渠道订单号", width = 25)
    private String upstreamOrderNo;

    @Excel(name = "商户订单号", width = 25)
    private String shopOrderNo;

    @Excel(name = "网厅流水号", width = 20)
    protected String orderSn;

    @Excel(name = "渠道名称", width = 30)
    private String upstreamName;

    @Excel(name = "商户名称", width = 30)
    private String shopName;

    @Excel(name = "手机号", width = 20)
    private String phoneNum;

    @Excel(name = "渠道订单价格", width = 20)
    private BigDecimal orderPrice;

    @Excel(name = "推送商户订单价格", width = 20)
    private BigDecimal shopPrice;

    @Excel(name = "运营商", replace = {"移动_100", "联通_200", "电信_300"}, width = 20)
    private String phoneOperator;

    @Excel(name = "订单超时时间(秒)", width = 20)
    private Integer orderExpireTime;

    @Excel(name = "平台订单状态", replace = {"推送失败_-1", "未使用_0", "已推送_1", "消费完成_2", "消费失败_3", "订单超时_4", "上游响应失败（亏损）_5"}, width = 30)
    private String platformOrderStatus;

    @Excel(name = "省份", width = 20)
    private String province;

    @Excel(name = "城市", width = 20)
    private String city;

    @Excel(name = "创建时间", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    private Date createTime;

    @Excel(name = "修改时间", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    private Date updateTime;

}
