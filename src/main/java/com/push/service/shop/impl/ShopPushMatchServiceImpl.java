package com.push.service.shop.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.webfacade.vo.platform.ShopPushMatchVO;
import com.push.dao.mapper.shop.ShopPushMatchMapper;
import com.push.entity.shop.ShopPushMatch;
import com.push.service.shop.ShopPushMatchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * Create DateTime: 2020/4/10 11:00
 *
 *

 */
@Service
public class ShopPushMatchServiceImpl extends ServiceImpl<ShopPushMatchMapper, ShopPushMatch> implements ShopPushMatchService {

    @Resource
    private ShopPushMatchMapper shopPushMatchMapper;

    @Override
    public List<ShopPushMatchVO> listWithShopName(Long matchClassifyId, String operatorType, BigDecimal money) {
        return shopPushMatchMapper.selectListWithShopName(matchClassifyId, operatorType, money);
    }
}
