package com.push.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.platform.SystemProvinceCity;

import java.util.List;

/**
 * Description:
 * Create DateTime: 2020-04-16 18:22
 *
 * 

 */
public interface SystemProvinceCityService extends IService<SystemProvinceCity> {

    void distribute(List<String> list, String phoneOperator);
}
