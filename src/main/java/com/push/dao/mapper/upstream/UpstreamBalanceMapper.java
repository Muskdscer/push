package com.push.dao.mapper.upstream;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.ListUpstreamUserInfoVO;
import com.push.common.webfacade.vo.platform.UpstreamUserInfoVO;
import com.push.entity.upstream.UpstreamBalance;
import com.push.entity.upstream.UpstreamUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description: 上游渠道用户余额Mapper
 * Create DateTime: 2020-03-26 11:33
 *
 *

 */
public interface UpstreamBalanceMapper extends BaseMapper<UpstreamBalance> {

}
