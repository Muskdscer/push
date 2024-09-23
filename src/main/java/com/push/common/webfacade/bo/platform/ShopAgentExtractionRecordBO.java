package com.push.common.webfacade.bo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.entity.BasePageBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 17:57
 *
 *

 */
@Data
public class ShopAgentExtractionRecordBO extends BasePageBO {
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //商户代理商名称
    private String shopAgentName;
    //商户手机号
    private String userMobile;
    //商户ID
    private Long shopAgentUserId;
}
