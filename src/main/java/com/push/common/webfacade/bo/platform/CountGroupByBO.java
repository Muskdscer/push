package com.push.common.webfacade.bo.platform;

import com.push.common.webfacade.bo.BaseBO;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/6/4 18:45
 *
 * 

 */
@Data
public class CountGroupByBO extends BaseBO {

    private Date startTime;

    @Override
    public void validate() {
        super.validate();
    }
}
