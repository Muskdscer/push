package com.push.common.webfacade.bo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import com.push.entity.BasePageBO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/27 19:09
 *
 * 

 */
@Data
public class ShopAgentUserPageBO extends BasePageBO {

    private String userName;

    private String shopAgentName;

    private String phoneNumber;

    private Date startTime;

    private Date endTime;

    @Override
    public void validate() {
        super.validate();
    }
}
