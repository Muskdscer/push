package com.push.entity.upstream;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description:
 * Create DateTime: 2020/3/31 19:16
 *
 * 

 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamSendOrderLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long upstreamUserId;

    private Integer pushNum;

    private String appId;

    private Integer status;

    private String remark;

    private String upstreamOrderNo;

    private String type;

    private String phoneOperator;

    private String phoneNum;
}
