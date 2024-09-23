package com.push.common.webfacade.bo.platform;

import com.push.entity.BasePageBO;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 9:51
 *
 *

 */
@Data
public class PlatformExtractionBO extends BasePageBO {

    private Date startTime;

    private Date endTime;

    private String operatorPerson;

    private String userMobile;

    @Override
    public void validate() {
        super.validate();
    }
}
