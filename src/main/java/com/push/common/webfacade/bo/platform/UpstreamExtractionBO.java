package com.push.common.webfacade.bo.platform;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 9:51
 *
 *

 */
@Data
public class UpstreamExtractionBO {
    private Date startTime;
    private Date endTime;
    private Long pageNo;
    private Long pageSize;
    private String upstreamName;
    private String userMobile;
    private Long upstreamUserId;
}
