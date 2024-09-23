package com.push.common.webfacade.bo.agent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.entity.BasePageBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 19:41
 *
 *

 */
@Data
public class ListAgentBillBO extends BasePageBO {

    //渠道代理商ID
    private Long userId;

    //交易人名称
    private String userName;

    //交易号
    private String tradeNo;

    //交易类型
    private String type;

    //开始时间
    private Date startTime;

    //结束时间
    private Date endTime;

    //交易状态
    private Integer status;

    @Override
    public void validate() {
        super.validate();
    }
}
