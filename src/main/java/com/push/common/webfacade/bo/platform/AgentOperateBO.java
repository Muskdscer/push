package com.push.common.webfacade.bo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.entity.BasePageBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/7 10:00
 *
 *

 */
@Data
public class AgentOperateBO extends BasePageBO {

    private String username;

    private Date startTime;

    private Date endTime;

    @Override
    public void validate() {
        super.validate();
    }
}
