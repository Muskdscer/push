package com.push.dao.mapper.platform;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.entity.platform.StatisticsBill;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 统计金额表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-04-22
 */
public interface StatisticsBillMapper extends BaseMapper<StatisticsBill> {

    BigDecimal getDealMoney(@Param("begin") DateTime todayBegin, @Param("end") DateTime todayEnd,
                            @Param("handleType") Integer handleType, @Param("status") Integer status,
                            @Param("userType") Integer userType);
}
