package com.push.service.shop;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.LoginLogBO;
import com.push.common.webfacade.vo.platform.LoginLogVO;
import com.push.entity.shop.ShopLogLogin;

/**
 * <p>
 * 商户登录日志表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopLogLoginService extends IService<ShopLogLogin> {

    Page<LoginLogVO> getShopLoginLog(LoginLogBO bo);
}
