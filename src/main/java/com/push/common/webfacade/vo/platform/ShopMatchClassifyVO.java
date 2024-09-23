package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/20 16:47
 *
 * 

 */
@Data
public class ShopMatchClassifyVO {

    private Long id;

    private String name;

    private String remark;

    private String value;

    private Date createTime;

    private Date updateTime;

}
