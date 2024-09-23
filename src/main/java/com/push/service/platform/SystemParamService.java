package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.SystemParam;

/**
 * Description:
 * Create DateTime: 2020-04-17 19:04
 *
 * 

 */
public interface SystemParamService extends IService<SystemParam> {

    /**
     * 修改版本
     */
    void modifyVersion();

}
