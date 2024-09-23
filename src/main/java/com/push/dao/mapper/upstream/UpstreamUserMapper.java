package com.push.dao.mapper.upstream;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.ListUpstreamUserInfoVO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.entity.upstream.UpstreamUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description: 上游渠道用户Mapper
 * Create DateTime: 2020-03-26 11:33
 *
 * 

 */
public interface UpstreamUserMapper extends BaseMapper<UpstreamUserInfo> {
    UpstreamUserInfoVO getOneWithMatchClassify(@Param("id") Long id, @Param("code") int code);


    List<ListUpstreamUserInfoVO> listAllUpstream(int code);

    IPage<UpstreamUserInfoVO> listUpstream(@Param("page") Page<UpstreamUserInfoVO> page, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("userName") String userName,
                                           @Param("phoneNumber") String phoneNumber, @Param("upstreamName") String upstreamName, @Param("matchClassifyId") Long matchClassifyId);

}
