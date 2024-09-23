package com.push.common.webfacade.bo.platform;

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
public class ShopUserPageBO extends BasePageBO {

    private String userName;

    private String shopName;

    private String phoneNumber;

    private Date startTime;

    private Date endTime;

    @Override
    public void validate() {
        super.validate();
    }
}
