package com.push.entity.upstream;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * 

 * @since 2020-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpstreamCallBack extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 平台订单号
     */
    private String platformOrderNo;

    /**
     * 渠道回调状态  0:失败   1:成功
     */
    private Integer status;




}
