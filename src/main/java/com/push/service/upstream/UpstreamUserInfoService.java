package com.push.service.upstream;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.UpstreamUserPageBO;
import com.push.common.webfacade.vo.platform.ListUpstreamUserInfoVO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.entity.upstream.UpstreamUserInfo;

import java.util.List;

/**
 * <p>
 * 上游用户表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface UpstreamUserInfoService extends IService<UpstreamUserInfo> {


    List<UpstreamUserInfo> getUpstreamUserByIds(List<Long> upstreamUserIds);

    UpstreamUserInfoVO getOneWithMatchClassify(Long id, int code);

    IPage<UpstreamUserInfoVO> listUpstream(UpstreamUserPageBO bo);

    List<ListUpstreamUserInfoVO> listAllUpstream(int code);
}
