package com.push.entity.platform;

import com.push.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 * Create DateTime: 2020/6/28 17:27
 *
 * 

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemBlackPhone extends BaseEntity {

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 状态 0 自动添加 1 手动添加
     */
    private Integer status;

}
