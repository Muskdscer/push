package com.push.entity.agent;

import com.push.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 上游代理商操作日志表
 * </p>
 *
 *

 * @since 2020-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UpstreamAgentLogOperate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上游代理商用户id
     */
    private Long upstreamAgentUserId;

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


}
