package com.push.service.upstream.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.bo.platform.UpstreamUserPageBO;
import com.push.common.webfacade.vo.platform.ListUpstreamUserInfoVO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.dao.mapper.upstream.UpstreamUserMapper;
import com.push.dao.upstream.UpstreamUserDao;
import com.push.entity.upstream.UpstreamUserInfo;
import com.push.service.upstream.UpstreamUserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 上游用户表 服务实现类
 * </p>
 *
 *

 * @since 2020-03-27
 */
@Service
public class UpstreamUserInfoServiceImpl extends ServiceImpl<UpstreamUserMapper, UpstreamUserInfo> implements UpstreamUserInfoService {

    @Resource
    private UpstreamUserDao upstreamUserDao;

    @Override
    public List<UpstreamUserInfo> getUpstreamUserByIds(List<Long> upstreamUserIds) {
        return upstreamUserDao.selectUpstreamUserByIds(upstreamUserIds);
    }

    @Override
    public UpstreamUserInfoVO getOneWithMatchClassify(Long id, int code) {
        return upstreamUserDao.getOneWithMatchClassify(id, code);
    }

    @Override
    public IPage<UpstreamUserInfoVO> listUpstream(UpstreamUserPageBO bo) {
        return upstreamUserDao.listUpstream(bo);
    }

    @Override
    public List<ListUpstreamUserInfoVO> listAllUpstream(int code) {
        return upstreamUserDao.listAllUpstream(code);
    }
}
