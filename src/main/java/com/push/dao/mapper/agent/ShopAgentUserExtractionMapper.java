package com.push.dao.mapper.agent;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.statistics.ShopAgentCheckExtractionVO;
import com.push.entity.agent.ShopAgentUserExtraction;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商户代理商提现表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-04-26
 */
public interface ShopAgentUserExtractionMapper extends BaseMapper<ShopAgentUserExtraction> {

    Page<ShopAgentCheckExtractionVO> getShopAgentExtractionToDoList(Page<Object> objectPage,
                                                                    @Param("status") Integer code,
                                                                    @Param("shopAgentUserId") Long shopAgentUserId,
                                                                    @Param("shopAgentName") String shopAgentName,
                                                                    @Param("userMobile") String userMobile,
                                                                    @Param("startTime") Date startTime,
                                                                    @Param("endTime") Date endTime);

    BigDecimal getShopAgentExtractionMoney(@Param("begin")DateTime todayBegin, @Param("end")DateTime todayEnd, @Param("status")Integer status);
}
