package com.push.service.upstream.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.OperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.dao.mapper.upstream.UpstreamLogOperateMapper;
import com.push.entity.upstream.UpstreamLogOperate;
import com.push.service.upstream.UpstreamLogOperateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 上游操作日志表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class UpstreamLogOperateServiceImpl extends ServiceImpl<UpstreamLogOperateMapper, UpstreamLogOperate> implements UpstreamLogOperateService {

    @Resource
    private UpstreamLogOperateMapper upstreamLogOperateMapper;

    @Override
    public IPage<OperateVO> listShopOperateLog(OperateBO bo) {
        IPage<OperateVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamLogOperateMapper.listPlatformOperateLog(page, bo.getUsername(), bo.getStartTime(), bo.getEndTime());
    }
}
