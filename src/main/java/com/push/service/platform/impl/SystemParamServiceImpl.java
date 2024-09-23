package com.push.service.platform.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.constants.ParamConstants;
import com.push.dao.mapper.platform.SystemParamMapper;
import com.push.entity.platform.SystemParam;
import com.push.service.platform.SystemParamService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020-04-17 19:05
 *
 *

 */
@Service
public class SystemParamServiceImpl extends ServiceImpl<SystemParamMapper, SystemParam> implements SystemParamService {

    @Resource
    private SystemParamMapper systemParamMapper;

    @Override
    public void modifyVersion() {
        systemParamMapper.update(new SystemParam(), Wrappers.lambdaUpdate(SystemParam.class)
                .eq(SystemParam::getName, ParamConstants.VERSION)
                .set(SystemParam::getValue, RandomStringUtils.randomNumeric(10)));
    }
}
