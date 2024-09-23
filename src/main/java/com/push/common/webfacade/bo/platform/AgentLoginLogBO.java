package com.push.common.webfacade.bo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.entity.BasePageBO;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/7 9:44
 *
 * 

 */
@Data
public class AgentLoginLogBO extends BasePageBO {
    private String userName;
    private String loginIP;
    private Date startTime;
    private Date endTime;

    @Override
    public void validate() {
        super.validate();
    }
}
