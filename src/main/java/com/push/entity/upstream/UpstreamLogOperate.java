package com.push.entity.upstream;

import com.push.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 上游操作日志表
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamLogOperate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上游用户id
     */
    private Long upstreamUserId;

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