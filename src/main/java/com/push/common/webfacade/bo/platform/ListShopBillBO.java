package com.push.common.webfacade.bo.platform;

import com.push.entity.BasePageBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 19:23
 *
 * 

 */
@Data
public class ListShopBillBO extends BasePageBO {

    //商户ID
    private Long shopUserId;

    //交易人名称
    private String userName;

    //交易号
    private String tradeNo;

    //开始时间
    private Date startTime;

    //结束时间
    private Date endTime;

    //交易类型
    private Integer type;

    //交易状态
    private Integer status;

    @Override
    public void validate() {
        super.validate();
    }
}
