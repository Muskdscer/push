package com.push.service.shop;

import com.baomidou.mybatisplus.extension.service.IService;
import com.push.entity.shop.CpShopBillInfo;

import java.util.List;

/**
 * <p>
 * 商户账单事务表 服务类
 * </p>
 *
 * 

 * @since 2020-03-27
 */
public interface CpShopBillInfoService extends IService<CpShopBillInfo> {

    List<CpShopBillInfo> findAllByState(Integer code);
}
