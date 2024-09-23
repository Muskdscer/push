package com.push.dao.upstream.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.bo.platform.UpstreamUserPageBO;
import com.push.common.webfacade.vo.platform.ListUpstreamUserInfoVO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.dao.mapper.upstream.UpstreamUserMapper;
import com.push.dao.upstream.UpstreamUserDao;
import com.push.entity.upstream.UpstreamUserInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: 上游渠道Dao实现类
 * Create DateTime: 2020-03-26 11:28
 *
 *

 */
@Repository
public class UpstreamUserDaoImpl implements UpstreamUserDao {

    @Resource
    private UpstreamUserMapper upstreamUserMapper;

    @Override
    public UpstreamUserInfo selectUpstreamUserByUserNameAndPassword(String username, String password) {
        Wrapper<UpstreamUserInfo> wrapper = Wrappers.lambdaQuery(UpstreamUserInfo.class)
                .eq(UpstreamUserInfo::getUserName, username)
                .eq(UpstreamUserInfo::getPassword, password);
        return upstreamUserMapper.selectOne(wrapper);
    }

    @Override
    public List<UpstreamUserInfo> selectUpstreamUserByIds(List<Long> upstreamUserIds) {
        return upstreamUserMapper.selectBatchIds(upstreamUserIds);
    }

    @Override
    public UpstreamUserInfoVO getOneWithMatchClassify(Long id, int code) {
        return upstreamUserMapper.getOneWithMatchClassify(id, code);
    }

    @Override
    public IPage<UpstreamUserInfoVO> listUpstream(UpstreamUserPageBO bo) {
        Page<UpstreamUserInfoVO> page = new Page<>(bo.getPageNo(), bo.getPageSize());
        return upstreamUserMapper.listUpstream(page, bo.getStartTime(), bo.getEndTime(), bo.getUserName(), bo.getPhoneNumber(),bo.getUpstreamName(),bo.getMatchClassifyId());
    }

    @Override
    public List<ListUpstreamUserInfoVO> listAllUpstream(int code) {
        return upstreamUserMapper.listAllUpstream(code);
    }
}
