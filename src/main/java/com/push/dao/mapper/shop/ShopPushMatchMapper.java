package com.push.dao.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.push.common.webfacade.vo.platform.ShopPushMatchVO;
import com.push.entity.shop.ShopPushMatch;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/4/10 11:02
 *
 * 

 */
public interface ShopPushMatchMapper extends BaseMapper<ShopPushMatch> {
    List<ShopPushMatchVO> selectListWithShopName(@Param("matchClassifyId") Long matchClassifyId, @Param("operatorType") String operatorType, @Param("money") BigDecimal money);

    List<ShopPushMatch> selectByShopIds(@Param("ids") List<Long> ids, @Param("deleteFlag") int deleteFlag,
                                        @Param("orderPrice") BigDecimal orderPrice, @Param("phoneOperator") String phoneOperator,
                                        @Param("aisleClassifyId") Long aisleClassifyId);
}
