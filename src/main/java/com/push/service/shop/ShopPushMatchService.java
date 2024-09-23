package com.push.service.shop;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.vo.platform.ShopPushMatchVO;
import com.push.entity.shop.ShopPushMatch;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/4/10 10:57
 *
 *

 */
public interface ShopPushMatchService extends IService<ShopPushMatch> {
    List<ShopPushMatchVO> listWithShopName(Long matchClassifyId, String operatorType, BigDecimal money);
}
