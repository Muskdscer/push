package com.push.service.shop;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.ShopExtractionRecordBO;
import com.push.common.webfacade.vo.platform.ShopCheckExtractionVO;
import com.push.entity.shop.ShopUserExtraction;
import com.push.entity.shop.ShopUserInfo;

/**
 * <p>
 * 商户提现表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopUserExtractionService extends IService<ShopUserExtraction> {

    Page<ShopCheckExtractionVO> getShopExtractionToDoList(Integer code, ShopExtractionRecordBO bo);

    Boolean operatorSuccess(ShopUserExtraction shopUserExtraction, ShopUserInfo shopUserInfo);

    boolean failShopUserExtraction(ShopUserExtraction shopUserExtraction);

    boolean saveExtractionAndUpdateUserInfo(ShopUserExtraction shopUserExtraction, ShopUserInfo shopUserInfo);
}
