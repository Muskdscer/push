package com.push.service.upstream.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.dao.mapper.upstream.UpstreamLogLoginMapper;
import com.push.entity.upstream.UpstreamLogLogin;
import com.push.service.upstream.UpstreamLogLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 上游登录日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class UpstreamLogLoginServiceImpl extends ServiceImpl<UpstreamLogLoginMapper, UpstreamLogLogin> implements UpstreamLogLoginService {
    @Resource
    private UpstreamLogLoginMapper upstreamLogLoginMapper;

    @Override
    public Page<LoginLogVO> getUpstreamLoginLog(LoginLogBO bo) {
        Page<LoginLogVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamLogLoginMapper.getUpstreamLoginLog(page, bo.getUserName(), bo.getLoginIp(), bo.getStartTime(), bo.getEndTime());
    }
}
