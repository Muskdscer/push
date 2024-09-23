package com.push.common.webfacade.bo.platform;

import com.push.entity.BasePageBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 11:58
 *
 *

 */
@Data
public class UpstreamUserPageBO extends BasePageBO {

    private String userName;

    private String upstreamName;

    private Long matchClassifyId;

    private String phoneNumber;

    private Date startTime;

    private Date endTime;

    @Override
    public void validate() {
        super.validate();
    }
}
