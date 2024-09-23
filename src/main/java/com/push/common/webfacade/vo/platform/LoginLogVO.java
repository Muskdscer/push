package com.push.common.webfacade.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.push.common.constants.DateConstant;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Create DateTime: 2020/4/7 13:13
 *
 * 

 */
@Data
public class LoginLogVO {
    private String userName;
    private String loginIp;
    private Integer loginStatus;
    private Date createTime;
    private Date updateTime;
}
