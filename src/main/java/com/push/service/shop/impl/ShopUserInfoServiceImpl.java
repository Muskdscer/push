package com.push.service.shop.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.push.common.utils.BeanUtils;
import com.push.common.utils.DateUtil;
import com.push.common.webfacade.bo.platform.ModifyShopUserInfoBO;
import com.push.common.webfacade.bo.platform.ShopUserPageBO;
import com.push.common.webfacade.vo.platform.ShopUserInfoVO;
import com.push.dao.mapper.shop.ShopUserMapper;
import com.push.entity.shop.ShopPushMatch;
import com.push.entity.shop.ShopUserInfo;
import com.push.service.shop.ShopPushMatchService;
import com.push.service.shop.ShopUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商户信息表 服务实现类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
@Service
public class ShopUserInfoServiceImpl extends ServiceImpl<ShopUserMapper, ShopUserInfo> implements ShopUserInfoService {

    @Resource
    private ShopUserMapper shopUserMapper;

    @Resource
    private ShopUserInfoService shopUserInfoService;

    @Resource
    private ShopPushMatchService shopPushMatchService;

    @Override
    @Transactional
    public Boolean updateAndPushMatchs(ModifyShopUserInfoBO bo) {
        ShopUserInfo shopUser = BeanUtils.copyPropertiesChaining(bo, ShopUserInfo::new);
        boolean update = shopUserInfoService.updateById(shopUser);
        boolean b = true;
        if (bo.getMatchClassifyId() != null) {
            List<ShopPushMatch> shopPushMatches = shopPushMatchService.list(Wrappers.lambdaQuery(ShopPushMatch.class).eq(ShopPushMatch::getShopId, bo.getId()));
            List<ShopPushMatch> pushMatches = shopPushMatches.stream().map(m -> m.setMatchClassifyId(bo.getMatchClassifyId())).collect(Collectors.toList());
            b = shopPushMatchService.updateBatchById(pushMatches);
        }
        return update && b;
    }

    @Override
    public ShopUserInfoVO getByIdWithMatch(Long id, int code) {
        return shopUserMapper.selectByIdWithMatch(id, code);
    }

    @Override
    public IPage<ShopUserInfoVO> pageWithMatch(ShopUserPageBO bo) {
        IPage<ShopUserInfoVO> page = new Page<>();
        return shopUserMapper.pageWithMatch(page, bo.getUserName(), bo.getShopName(), bo.getPhoneNumber(), bo.getEndTime(), bo.getStartTime());
    }

    @Override
    @Transactional
    public boolean deleteUserAndMatch(ShopPushMatch shopPushMatch, ShopUserInfo info) {
        boolean update = shopPushMatchService.update(shopPushMatch, Wrappers
                .lambdaUpdate(ShopPushMatch.class)
                .eq(ShopPushMatch::getShopId, info.getId()));
        boolean delete = shopUserInfoService.updateById(info);
        return update && delete;
    }
}
