package com.push.dao.mapper.upstream;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.CheckRechargeVO;
import com.push.entity.upstream.UpstreamUserRecharge;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 上游用户充值表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface UpstreamUserRechargeMapper extends BaseMapper<UpstreamUserRecharge> {

    Page<CheckRechargeVO> getUpstreamRechargeToDoList(Page<CheckRechargeVO> page, @Param("status") Integer code,
                                                      @Param("upstreamUserId") Long upstreamUserId,
                                                      @Param("upstreamName") String upstreamName,
                                                      @Param("userMobile") String userMobile,
                                                      @Param("startTime") Date startTime,
                                                      @Param("endTime") Date endTime);

    BigDecimal getUpstreamRechargeMoney(@Param("begin") DateTime todayBegin, @Param("end") DateTime todayEnd, @Param("status") Integer status);
}
