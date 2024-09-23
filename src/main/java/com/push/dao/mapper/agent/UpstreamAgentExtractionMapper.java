package com.push.dao.mapper.agent;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.AgentPassCheckExtractionVO;
import com.push.common.webfacade.vo.platform.AgentSuAndFaPassCheckExtractionVO;
import com.push.entity.agent.UpstreamAgentExtraction;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 上游代理商用户提现表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-04-26
 */
public interface UpstreamAgentExtractionMapper extends BaseMapper<UpstreamAgentExtraction> {

    IPage<AgentPassCheckExtractionVO> passCheckList(@Param("page") Page<AgentPassCheckExtractionVO> page, @Param("upstreamName") String upstreamName, @Param("phoneNumber") String phoneNumber, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("code") Integer code, @Param("userId") Long userId);

    IPage<AgentSuAndFaPassCheckExtractionVO> passSuAndFaCheckList(@Param("page") Page<AgentPassCheckExtractionVO> page, @Param("upstreamName") String upstreamName, @Param("phoneNumber") String phoneNumber, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("code") Integer code, @Param("userId") Long userId);

    BigDecimal getUpstreamAgentExtractionMoney(@Param("begin")DateTime todayBegin, @Param("end")DateTime todayEnd, @Param("status")Integer status);
}
