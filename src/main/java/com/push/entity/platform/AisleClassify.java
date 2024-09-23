package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 渠道管道分类表
 * </p>
 *
 * 

 * @since 2020-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AisleClassify extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 属性名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;


}
