package com.push.common.webfacade.vo.platform;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/23 17:15
 *
 *

 */
@Data
public class ExportShopAgentBillVO {
    @Excel(name = "交易号", width = 30)
    private String tradeNo;
    @Excel(name = "商户代理商名称", width = 30)
    private String userName;
    @Excel(name = "交易类型", replace = {"充值_1", "提现_2", "收入_3", "消费_4"}, width = 20)
    private Integer type;
    @Excel(name = "交易金额", width = 30)
    private BigDecimal money;
    @Excel(name = "交易状态", replace = {"未交易_0", "已交易_1"}, width = 20)
    private Integer status;
    @Excel(name = "创建时间",exportFormat = "yyyy-MM-dd HH:mm:ss", width = 30)
    private Date createTime;
    @Excel(name = "更新时间",exportFormat = "yyyy-MM-dd HH:mm:ss", width = 30)
    private Date updateTime;
}
