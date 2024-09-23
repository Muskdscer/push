package com.push.service.platform.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.annotation.TransactionalWithRollback;
import com.push.config.system.SystemConfig;
import com.push.dao.mapper.platform.SystemProvinceCityMapper;
import com.push.entity.platform.SystemProvinceCity;
import com.push.service.platform.SystemParamService;
import com.push.service.platform.SystemProvinceCityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020-04-16 18:22
 *
 * 

 */
@Service
public class SystemProvinceCityServiceImpl extends ServiceImpl<SystemProvinceCityMapper, SystemProvinceCity> implements SystemProvinceCityService {

    @Resource
    private SystemProvinceCityService systemProvinceCityService;

    @Resource
    private SystemParamService systemParamService;

    @Resource
    private SystemConfig systemConfig;

    @Override
    @TransactionalWithRollback
    public void distribute(List<String> list, String phoneOperator) {
        systemProvinceCityService.update(Wrappers.lambdaUpdate(SystemProvinceCity.class)
                .eq(SystemProvinceCity::getStatus, 1)
                .eq(SystemProvinceCity::getPhoneOperator,phoneOperator)
                .set(SystemProvinceCity::getStatus, 0));

        list.forEach(item -> {
            if (!item.startsWith("parent-")) {
                systemProvinceCityService.update(Wrappers.lambdaUpdate(SystemProvinceCity.class)
                        .eq(SystemProvinceCity::getCityCode, item)
                        .eq(SystemProvinceCity::getPhoneOperator,phoneOperator)
                        .set(SystemProvinceCity::getStatus, 1));
            }
        });

        systemConfig.load();
        systemParamService.modifyVersion();
    }
}
