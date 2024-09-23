package com.push.dao.mapper.platform;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.PlatformExtractionVO;
import com.push.entity.platform.PlatformExtraction;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 上游用户提现表 Mapper 接口
 * </p>
 *
 *

 * @since 2020-03-30
 */
public interface PlatformExtractionMapper extends BaseMapper<PlatformExtraction> {

    Page<PlatformExtractionVO> getPlatformExtractionToDoList(Page<PlatformExtractionVO> page, @Param("status") Integer code,
                                                             @Param("realName") String realName,
                                                             @Param("userMobile") String userMobile,
                                                             @Param("startTime") Date startTime,
                                                             @Param("endTime") Date endTime);

    BigDecimal getPlatformExtractionMoney(@Param("begin") DateTime todayBegin, @Param("end") DateTime todayEnd, @Param("status") Integer status);
}
