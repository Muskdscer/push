package com.push.common.webfacade.bo;

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
public class LoginLogBO extends BasePageBO {
    private String userName;
    private String loginIp;
    private Date startTime;
    private Date endTime;

    @Override
    public void validate() {
        super.validate();
    }
}
