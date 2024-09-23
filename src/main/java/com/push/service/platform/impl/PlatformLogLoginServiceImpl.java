package com.push.service.platform.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.dao.mapper.platform.PlatformLogLoginMapper;
import com.push.entity.platform.PlatformLogLogin;
import com.push.service.platform.PlatformLogLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class PlatformLogLoginServiceImpl extends ServiceImpl<PlatformLogLoginMapper, PlatformLogLogin> implements PlatformLogLoginService {

    @Resource
    private PlatformLogLoginMapper platformLogLoginMapper;

    @Override
    public Page<LoginLogVO> getPlatformLoginLog(LoginLogBO bo) {
        Page<LoginLogVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return platformLogLoginMapper.getPlatformLoginLog(page, bo.getUserName(), bo.getLoginIp(), bo.getStartTime(), bo.getEndTime());
    }
}
