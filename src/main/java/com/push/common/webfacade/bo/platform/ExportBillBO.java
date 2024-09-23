package com.push.common.webfacade.bo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.common.webfacade.bo.BaseBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/23 17:07
 *
 * 

 */
@Data
public class ExportBillBO extends BaseBO {

    //用户id
    private Long id;

    //交易人名称
    private String userName;

    //交易号
    private String tradeNo;

    //开始时间
    @DateTimeFormat(pattern = DateConstant.DATE_FORMAT_NORMAL)
    private Date startTime;

    //结束时间
    @DateTimeFormat(pattern = DateConstant.DATE_FORMAT_NORMAL)
    private Date endTime;

    @Override
    public void validate() {
        super.validate();
    }
}
