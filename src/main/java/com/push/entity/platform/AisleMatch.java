package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 渠道通道配置表
 * </p>
 *
 * 

 * @since 2020-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AisleMatch extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 商户id
     */
    private Long shopId;

    /**
     * 通道分类的id
     */
    private Long aisleClassifyId;

    /**
     * 备注
     */
    private String remark;


}
