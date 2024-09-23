package com.push.service.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.ModifyShopUserInfoBO;
import com.push.common.webfacade.bo.platform.ShopUserPageBO;
import com.push.common.webfacade.vo.platform.ShopUserInfoVO;
import com.push.entity.shop.ShopPushMatch;
import com.push.entity.shop.ShopUserInfo;

/**
 * <p>
 * 商户信息表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopUserInfoService extends IService<ShopUserInfo> {

    Boolean updateAndPushMatchs(ModifyShopUserInfoBO bo);

    ShopUserInfoVO getByIdWithMatch(Long id, int code);

    IPage<ShopUserInfoVO> pageWithMatch(ShopUserPageBO bo);

    boolean deleteUserAndMatch(ShopPushMatch shopPushMatch, ShopUserInfo info);
}
