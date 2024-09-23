package com.push.service.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.push.common.webfacade.bo.platform.OperateBO;
import com.push.common.webfacade.vo.platform.OperateVO;
import com.push.entity.shop.ShopLogOperate;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface ShopLogOperateService extends IService<ShopLogOperate> {

    IPage<OperateVO> listShopOperateLog(OperateBO bo);
}
