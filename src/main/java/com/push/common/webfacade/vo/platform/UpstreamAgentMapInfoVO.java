package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/26 18:00
 *
 *

 */
@Data
public class UpstreamAgentMapInfoVO {

    private Long id;

    /**
     * 通道名字
     */
    private String upstreamName;

    /**
     * 代理商手机号
     */
    private Long phoneNumber;

    /**
     * 对应渠道费率
     */
    private Double rate;

    private Date createTime;

    private Date updateTime;

}
