package com.push.dao.mapper.upstream;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.ShopCheckExtractionVO;
import com.push.common.webfacade.vo.platform.UpstreamCheckExtractionVO;
import com.push.entity.upstream.UpstreamExtraction;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 上游用户提现表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-03-27
 */
public interface UpstreamExtractionMapper extends BaseMapper<UpstreamExtraction> {


    Page<UpstreamCheckExtractionVO> getUpstreamExtractionToDoList(Page<ShopCheckExtractionVO> page, @Param("status") Integer code,
                                                                  @Param("upstreamUserId") Long upstreamUserId,
                                                                  @Param("upstreamName") String upstreamName,
                                                                  @Param("userMobile") String userMobile,
                                                                  @Param("startTime") Date startTime,
                                                                  @Param("endTime") Date endTime);

    BigDecimal getUpstreamExtractionMoney(@Param("begin") DateTime todayBegin, @Param("end") DateTime todayEnd, @Param("status") Integer status);
}
