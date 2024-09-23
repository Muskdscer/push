package com.push.dao.mapper.shop;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.push.common.webfacade.vo.platform.ShopCheckExtractionVO;
import com.push.entity.shop.ShopUserExtraction;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商户提现表 Mapper 接口
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopUserExtractionMapper extends BaseMapper<ShopUserExtraction> {

    Page<ShopCheckExtractionVO> getShopExtractionToDoList(Page<ShopCheckExtractionVO> page, @Param("status") Integer code,
                                                          @Param("shopUserId") Long shopUserId,
                                                          @Param("shopName") String shopName,
                                                          @Param("userMobile") String userMobile,
                                                          @Param("startTime") Date startTime,
                                                          @Param("endTime") Date endTime);

    BigDecimal getShopExtractionMoney(@Param("begin") DateTime todayBegin, @Param("end") DateTime todayEnd, @Param("status") Integer status);
}
