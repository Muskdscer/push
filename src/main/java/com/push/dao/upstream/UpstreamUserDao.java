package com.push.dao.upstream;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.push.common.webfacade.bo.platform.UpstreamUserPageBO;
import com.push.common.webfacade.vo.platform.ListUpstreamUserInfoVO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.entity.upstream.UpstreamUserInfo;

import java.util.List;

/**
 * Description: 上游渠道Dao接口
 * Create DateTime: 2020-03-26 11:27
 *
 * 

 */
public interface UpstreamUserDao {

    /**
     * 根据用户名和密码查询上游渠道信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 上游渠道信息
     */
    UpstreamUserInfo selectUpstreamUserByUserNameAndPassword(String username, String password);

    List<UpstreamUserInfo> selectUpstreamUserByIds(List<Long> upstreamUserIds);

    UpstreamUserInfoVO getOneWithMatchClassify(Long id, int code);

    IPage<UpstreamUserInfoVO> listUpstream(UpstreamUserPageBO bo);

    List<ListUpstreamUserInfoVO> listAllUpstream(int code);
}
