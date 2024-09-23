package com.push.common.webfacade.bo.platform;

import com.push.entity.BasePageBO;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/3/30 17:57
 *
 *

 */
@Data
public class ShopExtractionRecordBO extends BasePageBO {
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //商户名称
    private String shopName;
    //商户手机号
    private String userMobile;
    //商户ID
    private Long shopUserId;

    @Override
    public void validate() {
        super.validate();
    }
}
