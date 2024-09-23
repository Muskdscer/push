package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/26 16:46
 *
 *

 */
@Data
public class ShopAgentMapVO {

    private Long mapId;
    private Long shopId;

    /**
     * 商户企业名称
     */
    private String shopName;

    /**
     * 商户手机号
     */
    private String phoneNumber;

    /**
     * 费率
     */
    private Double rate;

    private Date createTime;

    private Date updateTime;


}
