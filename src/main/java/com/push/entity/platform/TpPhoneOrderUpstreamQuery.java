package com.push.entity.platform;

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

 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TpPhoneOrderUpstreamQuery extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;



    private String upstreamOrderNo;

    private String platformOrderNo;

    private Integer beforeQueryOrderStatus;

    private Integer afterQueryOrderStatus;




}
