package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.enums.BlackPhoneEnum;
import com.push.common.webfacade.bo.out.UpstreamSendOrderBO;
import com.push.config.system.SystemConfig;
import com.push.dao.mapper.platform.SystemBlackPhoneMapper;
import com.push.entity.platform.SystemBlackPhone;
import com.push.service.platform.SystemBlackPhoneService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * Create DateTime: 2020/6/28 17:30
 *
 *

 */
@Service
public class SystemBlackPhoneServiceImpl extends ServiceImpl<SystemBlackPhoneMapper, SystemBlackPhone> implements SystemBlackPhoneService {

    @Resource
    private SystemBlackPhoneMapper systemBlackPhoneMapper;

    @Resource
    private SystemConfig systemConfig;

    @Override
    public void saveAndReload(UpstreamSendOrderBO bo) {
        SystemBlackPhone blackPhone = new SystemBlackPhone();
        blackPhone.setPhoneNum(bo.getPhoneNum());
        blackPhone.setStatus(BlackPhoneEnum.AUTO.getCode());
        systemBlackPhoneMapper.insert(blackPhone);

        //重新加载黑名单如redis
        systemConfig.loadBlackPhone();
    }
}
