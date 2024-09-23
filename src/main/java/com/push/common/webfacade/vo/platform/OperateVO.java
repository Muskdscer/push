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
public class OperateVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 操作描述
     */
    private String operationDescribe;
    /**
     * 操作反馈
     */
    private String operationFeedback;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
